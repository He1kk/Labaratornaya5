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
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebServlet(name = "OrderCreateServlet", urlPatterns = "/orders/new")
public class OrderCreateServlet extends HttpServlet {

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

        webContext.setVariable("order", new Order());
        templateEngine.process("order-form", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String status = req.getParameter("status");
        String shippingAddress = req.getParameter("shippingAddress");

        Order order = new Order();
        order.setStatus(status);
        order.setShippingAddress(shippingAddress);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(BigDecimal.ZERO);

        orderRepository.save(order);

        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}