package store.Controller;

import store.Repository.ProductInventory;

public class StoreController {
    private final InputController inputController;
    private final ProductInventory productInventory;


    public StoreController() {
        inputController=new InputController();
        productInventory=new ProductInventory();
    }


}

