package com.example.p_backendsigmaorder.IA;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.example.p_backendsigmaorder.Producto.domain.Producto;
import com.example.p_backendsigmaorder.Producto.domain.ProductoService;
import com.example.p_backendsigmaorder.Promocion.Service.PromocionService;
import com.example.p_backendsigmaorder.Promocion.domain.Promocion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ConditionalOnProperty(name = "app.chat.enabled", havingValue = "true", matchIfMissing = true)
@Service
public class ChatService {
    private final ChatCompletionsClient client;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PromocionService promocionService;

    public ChatService() {
        String key = System.getenv("AZURE_API_KEY");
        String endpoint = "https://models.github.ai/inference";

        this.client = new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential(key))
                .endpoint(endpoint)
                .buildClient();
    }

    public String chat(String userPrompt) {
        List<Producto> productos = productoService.findAll();
        List<Promocion> promociones = promocionService.findActive();

        // Build inventory context
        StringBuilder contexto = new StringBuilder("Inventario TamboTec:\n");
        for (Producto p : productos) {
            contexto.append(String.format(
                    "- [%d] %s: S/%.2f (cat: %s, stock: %d)\n",
                    p.getId(), p.getNombre(), p.getPrecio(),
                    p.getCategoria(), p.getStock()
            ));
        }
        contexto.append("\nPromociones activas:\n");
        for (Promocion prom : promociones) {
            contexto.append(String.format(
                    "- %s: %.0f%% de descuento en productos %s\n",
                    prom.getCodigoPromocion(),
                    prom.getPorcentajeDescuento(),
                    prom.getProductos().stream()
                            .map(prod -> prod.getNombre() + " (ID " + prod.getId() + ")")
                            .collect(Collectors.joining(", "))
            ));
        }

        // System message with traditional string concatenation for Java 11
        String systemMessage = "Eres el asistente virtual de TamboTec, una tienda de abarrotes peruana.\n" +
                "Tu función es ayudar a los clientes a encontrar productos y promociones disponibles en la tienda según lo que deseen comprar.\n" +
                "Solo debes sugerir productos que estén en el inventario actual y promociones activas.\n\n" +
                "**IMPORTANTE:** Cada producto o promoción que menciones debe incluir siempre su ID para que el usuario pueda identificarlo fácilmente.\n" +
                "Cuando hables de productos, incluye: nombre, ID, precio, categoría y stock.\n" +
                "Cuando menciones promociones, incluye: código de promoción, porcentaje de descuento, productos incluidos con sus respectivos nombres e IDs.\n\n" +
                "Si el usuario pregunta por productos relacionados con un plato, revisa las promociones activas y menciona las que incluyan esos productos por nombre, ID y descuento.\n\n" +
                "Sé muy amable y cordial, como un asistente virtual peruano que dice \"caserito\", \"amigo\", etc.\n\n" +
                "Si no hay productos o promociones que coincidan, responde amablemente que aún no hay disponibles para eso.";

        List<ChatRequestMessage> messages = Arrays.asList(
                new ChatRequestSystemMessage(systemMessage),
                new ChatRequestSystemMessage(contexto.toString()),
                new ChatRequestUserMessage(userPrompt)
        );

        ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
        String model = "openai/gpt-4.1-nano";
        options.setModel(model);

        ChatCompletions completions = client.complete(options);
        if (completions.getChoices() != null && !completions.getChoices().isEmpty()) {
            return completions.getChoices().get(0).getMessage().getContent();
        }
        return "No se recibieron respuestas del modelo.";
    }
}