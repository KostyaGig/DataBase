package com.kostya_zinoviev.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import Data.DataBase;
import Model.Car;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Coздаем обЪект класса DataBase,который является субклассом SQLiteOpenHelper
        DataBase db = new DataBase(this);
        //Начнем добавлять записи в нашу бд
        //Используем 2 конструктор,т.к id в SQL добавляется по default

        db.addCar(new Car("Mercedes","100 000 $"));
        db.addCar(new Car("Mercedes","1454 000 $"));
        db.addCar(new Car("Mercedes","1050 000 $"));
        db.addCar(new Car("Mercedes","10780 000 $"));
        List<Car> carList = db.getAllCars();
        Car car3 = new Car("Kostya","849000000000 $ ");
        db.addCar(car3);
        db.deleteCar(car3);
            for(Car car:carList){
                Log.d("Car","id " + car.getId() + " name " + car.getName() + " price " + car.getPrice() + car3.getName());
            }
            //C помощью метода getCar(id),который находится в классе DataBase мы можем получать машину по id,после записи соответственно
            //Car car = db.getCar(1);
            //Метод upDateCar,использование:
            //C помощью него мы будем обновлять информацию о нашей машине
            //car.setName("Tesla");
            //car.setPrice("1303030303 $");
            //Обновляем
            //db.updateCar(car);
    }
}
