package com.epam.esm.exception.notfound;

public class CertificateNotFoundException extends EntityNotFoundException {


    public CertificateNotFoundException(String errorCode, String id) {
        super(errorCode, id);
    }


}
