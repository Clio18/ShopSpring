package com.luxcampus.web.controller;
import com.luxcampus.entity.Product;
import com.luxcampus.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    //get all products
    @RequestMapping(path = "/products", method = RequestMethod.GET)
    protected String findAll(Model model) {
        List<Product> products = productService.get();
        model.addAttribute("products", products);
        return "products";
    }

    //show form for adding
    @RequestMapping(path = "/products/add", method = RequestMethod.GET)
    protected String getAdd() {
        return "addProduct";
    }

    //adding the product
    @RequestMapping(path = "/products/add", method = RequestMethod.POST)
    protected String addProduct(@RequestParam String name,
                                @RequestParam double price) {
        Product product = Product.builder().name(name).price(price).build();
        productService.save(product);
        return "redirect:/products";
    }

    //show delete
    @RequestMapping(path = "/products/delete", method = RequestMethod.GET)
    protected String getDelete () {
       return "deleteProduct";
    }

    //delete product
    @RequestMapping(path = "/products/delete", method = RequestMethod.POST)
    protected String deleteProduct(@RequestParam int id){
            productService.delete(id);
            return "redirect:/products";
    }

    // get update
    @RequestMapping(path = "/products/update", method = RequestMethod.GET)
    protected String getUpdate(@RequestParam int id, Model model) {
        Product product = productService.getById(id);
        model.addAttribute("product", product);
        return "updateProduct";
    }

    // update product
    @RequestMapping(path = "/products/update", method = RequestMethod.POST)
    protected String updateProduct (@RequestParam int id,
                             @RequestParam String name,
                             @RequestParam double price) {
            Product product = Product.builder().id(id).name(name).price(price).build();
            productService.update(product);
            return "redirect:/products";
    }
}
