package com.e_commerceapp.clothshops.service.product;

import com.e_commerceapp.clothshops.dto.ProductDTO;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.mapper.ProductMapper;
import com.e_commerceapp.clothshops.model.Category;
import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.repository.CategoryRepository;
import com.e_commerceapp.clothshops.repository.ProductRepository;
import com.e_commerceapp.clothshops.requests.ProductRequests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
//this generates constructor for final or not null fields
//suitable for inject dep
//@RequiredArgsConstructor
@Transactional
public class ProductService implements IProductService {



    private final ProductRepository productRepository;


    private final CategoryRepository categoryRepository;


    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product addProduct(ProductRequests productRequests) {
        //check if category is found in the DB
        //if yes, set it as new product category
        //if no, then save it as a new category
        //then set it as a new product category.
        Category category = Optional.ofNullable(categoryRepository
                //orElseGet this mean if cat not there do something
                .findByName(productRequests.getCategory().getName())).orElseGet(
                () -> {
                    Category newCategory = new Category(productRequests.getCategory().getName());
                    return categoryRepository.save(newCategory);
                }
        );
        /*
        .orElseGet: if category not there then it will do the action but these action must return
        the same type of object that call it
        so if we say String.orElseGet(()->{ this must return String type});
        and above it will return category after saving it in db and will but it in category(object you made) and then
        product will take it as a setCategory
        all of this if category you're asking is not exist
         */
        productRequests.setCategory(category);
        return productRepository.save(productMapper.createProductFromProductReq(productRequests));
    }


    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no product with id:" + productId
                )
        );
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.findById(productId)
                .ifPresentOrElse(productRepository::delete,
                        () -> {
                            throw new GlobalNotFoundException(
                                    "there is no product with id:" + productId
                            );
                        });
//        Product product = new Product();
//        product = productRepository.findById(productId).orElseThrow(
//                () -> new GlobalNotFoundException(
//                        "there is no product with id:" + productId
//                )
//        );
//        productRepository.delete(product);
    }

    @Override
    public ProductDTO updateProduct(ProductRequests productRequests, Long productId) {
//        Product product = productRepository.findById(productId).orElseThrow(
//                () -> new GlobalNotFoundException("there is no product with id:" + productId)
//        );
//    return updateExistingProduct(product,productDTO).map(productRepository::save);
        Product product = productRepository.findById(productId).map(
                //existingProduct is the one that will found from findById
                existingProduct -> updateExistingProduct(existingProduct, productRequests)
        ).map(productRepository::save).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no product with id: " + productId
                )
        );
        /*
        .map(productRepository::save)
        is like calling save method on the instance that call map function
        it mean
        .map(updatedProduct -> productRepository.save(updatedProduct))
         */
        return productMapper.createProductDTOFromProduct(product);
    }

    private Product updateExistingProduct(
            Product existingProduct,
            ProductRequests productRequests
    ) {
        existingProduct.setName(productRequests.getName());
        existingProduct.setBrand(productRequests.getBrand());
        existingProduct.setPrice(productRequests.getPrice());
        existingProduct.setInventory(productRequests.getInventory());
        existingProduct.setDescription(productRequests.getDescription());

        Category category = categoryRepository.findByName(productRequests.getCategory().getName());
        existingProduct.setCategory(category);

//        existingProduct = productMapper.createProductFromProductDTO(productDTO, category);

        return existingProduct;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products =  productRepository.findAll();
        return productMapper.createListOfProductDTOFromListOfProduct(products);
//                .stream().map(productMapper::createProductDTOFromProduct).toList();
    }

    @Override
    public List<ProductDTO> getAllProductsByCategoryName(String categoryName) {
        List<Product> products = productRepository.findByCategoryName(categoryName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    @Override
    public List<ProductDTO> getAllProductsByBrandName(String brandName) {
        List<Product> products = productRepository.findByBrand(brandName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    @Override
    public List<ProductDTO> getAllProductsByCategoryNameAndBrandName(String categoryName, String brandName) {
        List<Product> products = productRepository.findByCategoryNameAndBrand(categoryName, brandName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    @Override
    public List<ProductDTO> getProductsByName(String productName) {
        List<Product> products = productRepository.findByName(productName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    @Override
    public List<ProductDTO> getProductsByBrandNameAndProductName(String productName, String brandName) {
        List<Product> products = productRepository.findByBrandAndName(productName, brandName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    @Override
    public List<ProductDTO> getProductsByCategoryNameAndProductName(String productName, String categoryName) {
        List<Product> products = productRepository.findByCategoryNameAndName(categoryName, productName);
        return productMapper.createListOfProductDTOFromListOfProduct(products);
    }

    @Override
    public Long countProductByBrandNameAndProductName(String productName, String brandName) {
        return productRepository.countByBrandAndName(productName, brandName);
    }

    @Override
    public Long countProductByCategoryNameAndProductName(String productName, String categoryName) {
        return productRepository.countByCategoryNameAndName(categoryName, productName);
    }
}
