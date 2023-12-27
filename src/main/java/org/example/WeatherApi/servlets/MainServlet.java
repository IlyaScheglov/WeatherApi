package org.example.WeatherApi.servlets;

import org.example.WeatherApi.components.WeatherGetter;
import org.example.WeatherApi.config.WaysConfig;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@WebServlet(urlPatterns = {"/city-weather"})
public class MainServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Map<String, String[]> parametersMap = req.getParameterMap();
        resp.setContentType("text/html");
        if(parametersMap.get("city") == null){
            resp.getWriter().write(WaysConfig.getFirstHTMLPage());
        }
        else{
            String cityName = parametersMap.get("city")[0];
            String weather = WeatherGetter.getWetherInCity(cityName);
            String strToWrite = WaysConfig.getSecondHTMLPage()
                    .replaceAll("<div></div>", weather);
            resp.getWriter().write(strToWrite);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
