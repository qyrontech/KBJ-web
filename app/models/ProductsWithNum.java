package models;

import java.util.List;

public class ProductsWithNum {

    private Long numFound;
    private List<Product> products;

    public ProductsWithNum(Long numFound, List<Product> products) {
        this.products = products;
        this.numFound = numFound;
    }

    public Long getNumFound() {
        return numFound;
    }

    public void setNumFound(Long numFound) {
        this.numFound = numFound;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
