package com.oopclass.breadapp.views;

import java.util.ResourceBundle;

/**
 * OOP Class 20-21
 *
 * @author Gerald Villaran
 */
public enum FxmlView {

    USER {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("user.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/User.fxml";
        }
    }, PRODUCT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("product.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Product.fxml";
        }
    }, PROJECT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("project.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Project.fxml";
        } 
    }, CLIENT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("client.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Client.fxml";
        }
    }, CONTRACT {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("contract.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/Contract.fxml";
        }
    };
    

    public abstract String getTitle();

    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key) {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}