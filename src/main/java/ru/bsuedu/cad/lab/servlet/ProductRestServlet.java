package ru.bsuedu.cad.lab.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProductRestServlet", urlPatterns = "/api/products")
public class ProductRestServlet extends HttpServlet {

    private ProductRepository productRepository;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        var context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.productRepository = context.getBean(ProductRepository.class);
        this.objectMapper = context.getBean(ObjectMapper.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        List<ProductDto> dtoList = new ArrayList<>();
        Iterable<Product> products = productRepository.findAll();

        for (Product p : products) {
            String categoryName = (p.getCategory() != null) ? p.getCategory().getName() : "Без категории";
            dtoList.add(new ProductDto(p.getName(), categoryName, p.getStockQuantity()));
        }

        objectMapper.writeValue(resp.getWriter(), dtoList);
    }

    public static class ProductDto {
        private final String name;
        private final String categoryName;
        private final Integer stockQuantity;

        public ProductDto(String name, String categoryName, Integer stockQuantity) {
            this.name = name;
            this.categoryName = categoryName;
            this.stockQuantity = stockQuantity;
        }

        public String getName() { return name; }
        public String getCategoryName() { return categoryName; }
        public Integer getStockQuantity() { return stockQuantity; }
    }
}