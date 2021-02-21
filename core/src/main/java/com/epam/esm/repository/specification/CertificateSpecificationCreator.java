package com.epam.esm.repository.specification;


import com.epam.esm.repository.specification.impl.certificate.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CertificateSpecificationCreator {

    private List<Specification> specifications;
    private final Map<String, String> orderByQueries;

    {
        orderByQueries = new HashMap<>();
        orderByQueries.put(CreatorParams.SORT_BY_NAME_ASC, CreatorParams.NAME_FIELD);
        orderByQueries.put(CreatorParams.SORT_BY_NAME_DESC, CreatorParams.NAME_FIELD);
        orderByQueries.put(CreatorParams.SORT_BY_DATE_ASC, CreatorParams.CREATED_DATE_FIELD);
        orderByQueries.put(CreatorParams.SORT_BY_DATE_DESC, CreatorParams.CREATED_DATE_FIELD);
    }

    public List<Specification> generateQueryCriteria(Map<String, String> params) {
        specifications = new ArrayList<>();
        params.keySet().forEach(key -> {
            switch (key) {
                case CreatorParams.CERTIFICATE_NAME_PARAM:
                    specifications.add(new GetCertificatesByName(params.get(key)));
                    break;
                case CreatorParams.CERTIFICATE_DESCRIPTION_PARAM:
                    specifications.add(new GetCertificatesByDescription(params.get(key)));
                    break;
                case CreatorParams.TAG_PARAM:
                    addTagNamePredicate(params.get(key));
                    break;
                case CreatorParams.ORDER_BY_PARAM:
                    addOrderBy(params);
                default:
                    break;
            }
        });
        return specifications;
    }

    private void addTagNamePredicate(String tagNamesAsString) {
        String[] tagNames = tagNamesAsString.split(CreatorParams.COMMA);
        for (String tagName : tagNames) {
            specifications.add(new GetCertificatesByTagName(tagName.trim()));
        }
    }

    private void addOrderBy(Map<String, String> params) {
        String value = params.get(CreatorParams.ORDER_BY_PARAM);

        if (value.startsWith("-")) {
            specifications.add(new SortCertificatesDescending(orderByQueries.get(value)));
        } else {
            specifications.add(new SortCertificatesAscending(orderByQueries.get(value)));
        }
    }


    private static class CreatorParams {
        private static final String ORDER_BY_PARAM = "orderBy";
        private static final String TAG_PARAM = "tag";
        private static final String CERTIFICATE_NAME_PARAM = "name";
        private static final String CERTIFICATE_DESCRIPTION_PARAM = "description";
        private static final String SORT_BY_NAME_ASC = "name";
        private static final String SORT_BY_NAME_DESC = "-name";
        private static final String SORT_BY_DATE_ASC = "date";
        private static final String SORT_BY_DATE_DESC = "-date";
        private static final String NAME_FIELD = "name";
        private static final String CREATED_DATE_FIELD = "createDate";
        private static final String COMMA = ",";
    }
}
