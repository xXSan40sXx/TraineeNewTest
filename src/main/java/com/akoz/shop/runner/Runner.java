package com.akoz.shop.runner;

import com.akoz.shop.entity.Shop;
import com.akoz.shop.persistence.ShopDAO;
import com.akoz.shop.persistence.exception.ShopException;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author akoz
 */
public class Runner {

    public static void main(String[] args) {
        try {
            ShopDAO shopDAO = ShopDAO.INSTANCE;

            List<Shop> shops = shopDAO.findAllShops();
            if (shops.size() < 2) {
                System.out.println("Not enough shops in DB");
                return;
            }

            Thread thread1 = new Thread(new ShopTask(shops.get(0)));
            Thread thread2 = new Thread(new ShopTask(shops.get(1)));

            thread1.start();
            TimeUnit.SECONDS.sleep(10);
            thread2.start();

            thread1.join();
            thread2.join();

            System.out.println("Done!");
        } catch (ShopException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
