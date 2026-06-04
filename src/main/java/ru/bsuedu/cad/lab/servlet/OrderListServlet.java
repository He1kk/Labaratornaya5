package ru.bsuedu.cad.lab.servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import java.io.IOException;

@WebServlet(name = "OrderListServlet", urlPatterns = "/orders")
public class OrderListServlet extends HttpServlet {

    private OrderRepository orderRepository;
    private TemplateEngine templateEngine;
    private JakartaServletWebApplication application;

    @Override
    public void init() throws ServletException {
        var context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.orderRepository = context.getBean(OrderRepository.class);
        this.templateEngine = context.getBean(TemplateEngine.class);
        this.application = JakartaServletWebApplication.buildApplication(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        var exchange = application.buildExchange(req, resp);
        var webContext = new WebContext(exchange, req.getLocale());

        webContext.setVariable("orders", orderRepository.findAll());

        templateEngine.process("orders-list", webContext, resp.getWriter());
    }
}