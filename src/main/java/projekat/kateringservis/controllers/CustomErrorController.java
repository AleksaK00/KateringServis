package projekat.kateringservis.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {

        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object exceptionType = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);

        int status = 500;
        String error = "Unknown Error";

        if (statusCode != null) {
            status = Integer.parseInt(statusCode.toString());
            HttpStatus httpStatus = HttpStatus.valueOf(status);
            error = httpStatus.getReasonPhrase();
        }

        model.addAttribute("status", status);
        model.addAttribute("error", error);
        model.addAttribute("message", errorMessage != null ? errorMessage : "No message available");
        model.addAttribute("path", requestUri != null ? requestUri : "Unknown");

        return "error/error";
    }

}
