open module kinesheet.rest {
    requires com.fasterxml.jackson.databind;

    requires kinesheet.model;
    
    requires lombok;

    requires jakarta.servlet;
    requires jakarta.validation;

    requires org.apache.commons.lang3;

    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.security.oauth2.core;
    requires spring.security.oauth2.jose;
    requires spring.security.oauth2.resource.server;
    requires spring.security.config;
    requires spring.security.core;
    requires spring.security.web;
    requires spring.web;
    requires spring.webmvc;
}
