package com.hrushev.controllers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrushev.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public String confirm(Model model, @RequestParam String username, @RequestParam String password, @RequestParam String address, @RequestParam String products) {
        System.out.println(username);
        System.out.println(password);
        System.out.println(address);
        System.out.println(products);

        return "home";
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String confirmOrder(Model model, @RequestBody String products) {
        model.addAttribute("productList", parseProductList(products));

        return "orderConfirmation";
    }

    private List<Product> parseProductList(String products) {
        List<Product> productList = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

            JsonNode listNode = mapper.readTree(products);

            for (int i = 0; i < listNode.size(); i++) {
                JsonNode productNode = listNode.get(i);

                int id = productNode.get("id").asInt();
                String name = productNode.get("name").asText();
                double price = productNode.get("price").asDouble();
                String desc = productNode.get("description").asText();
                int quantity = productNode.get("quantity").asInt();

                productList.add(new Product(id, name, price, desc, quantity));
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return productList;
    }
}
