package com.epam.esm.repository.specification;

import com.epam.esm.repository.specification.impl.order.GetOrdersByUserId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OrderSpecificationCreator {
    private List<SearchSpecification> specifications;
    private final static String USER_ID = "userId";

    public List<SearchSpecification> generateQuery(Map<String, String> params) {
        specifications = new ArrayList<>();
        appendQueryConditions(params);
        return specifications;
    }

    private void appendQueryConditions(Map<String, String> params) {
        params.keySet().forEach(key -> {
            if (USER_ID.equals(key)) {
                specifications.add(new GetOrdersByUserId(Integer.parseInt(params.get(key))));
            }
        });
    }
}
