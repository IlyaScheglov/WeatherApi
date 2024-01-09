package org.example.WeatherApi.servlets;

import org.example.WeatherApi.components.WeatherGetter;
import org.example.WeatherApi.config.WaysConfig;
import org.example.WeatherApi.wetherobject.WeatherObject;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            List<WeatherObject> weather = WeatherGetter.getWetherInCity(cityName);
            StringBuilder weatherDIV = new StringBuilder();
            weather.forEach(w -> weatherDIV.append(w.toDivHTML()));
            List<Integer> listTemperatureToAnalyze = weather.stream()
                    .map(w -> w.getTemperature())
                    .collect(Collectors.toList());
            String weatherChanges = WeatherGetter.weatherChangesAnalitic(cityName, listTemperatureToAnalyze);
            String strToWrite = WaysConfig.getSecondHTMLPage()
                    .replaceAll("XXX", "Погода в городе: " + cityName)
                    .replaceAll("ZZZ", weatherChanges)
                    .replaceAll("<div></div>", weatherDIV.toString());
            resp.getWriter().write(strToWrite);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
