package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import Model.Car;
import Utils.Util;


public class DataBase extends SQLiteOpenHelper {
//Удаляем  ненужные нам параметры конструктора и оставляем контекст

    public DataBase(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Вызывается,когда мы хотим создать в 1 раз нашу БД
        //SQL - Structed(структурированный) Query(Запрос) Language(Язык)
        //Ниже код,написанный на языке SQL
        //Начинаем создание таблицы с кл. слова CREATE TABLE !!!Учитываем все пробелы!!! ,после пишем название таблицы
        //Затем открываем скобочку ")" и начинаем перечислять название столбцов
        //INTEGER  - тип столбца (целочисленный)
        //PRIMARY KEY - значит,что по значению данного поля мы будем обращаться к нашим полям
        //TEXT - тип String в SQL
        String CREATE_CARD_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY,"
                + Util.KEY_NAME + " TEXT,"
                + Util.KEY_PRICE + " TEXT" + ")";
        //Мы создали структуру таблицы,но она пуста
        //Создаем БД ниже,с помощью метода execSQL(), в параметры которого предаем нашу раннее созданную строку
        // db.execSQL - запустить строку
        db.execSQL(CREATE_CARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Вызывается,когда мы хотим обновить нашу раннее созданную БД
        //Ниже так же язык SQL
        //Код ниже удаляет старую,и создает новую бд (обновляет)
        /*db.execSQL("DROP TABLE IF EXISTS "+Util.TABLE_NAME);
        onCreate(db);*/
    }
   /* CRUD - (Операции в SQL),значит :
    С - creat(создание)
    R - read(считывание)
    U - update(обновление)
    D - delete(удаление)    */

    //Создадим ниже метод,с помощью которого мы будем добавлять нашу машину в таблицу
    public void addCar(Car car) {
        //Создаем обЪект класса  SQLiteDatabase,метод getWritableDatabase() поможет записать нам данные в таблицу
        SQLiteDatabase db = this.getWritableDatabase();
        //С помощью класса ContentValues мы будем записывать данные таблицу в виде пар key/value
        ContentValues cv = new ContentValues();
        //1 параметр - ключ,в нашем случае имя столбца , с помощью обЪуекта класса Car мы будем получать имя - car.getName()
        cv.put(Util.KEY_NAME, car.getName());
        cv.put(Util.KEY_PRICE, car.getPrice());
        //Не помещаем ID,т.к в SQL ID автоматически добавляется и инкрементируется
        //Помещаем данные в бд с помощью метода insert(),1 параметр которого название таблицы,2 null,3 наш класс ContentValues
        db.insert(Util.TABLE_NAME, null, cv);
        //После мы должны разорвать соединение с БД,с помощью метода close()
        db.close();
    }

    //Создадим ниже метод,с помощью которого мы будем извлекать поля нашей ОДНОЙ машины из таблицы
    public Car getCar(int id) {
        //Будем извлекать наши данные с помозью ID,поэтому передаем в параметр метода id
        //Также создаем  обЪект класса  SQLiteDatabase ,метод getReadableDatabase() поможет  нам извлечь данные из таблицы
        SQLiteDatabase db = this.getReadableDatabase();
        //С помощью класса Cursor мы будем извлекать  данные из таблицы
        // db.query(считывание данных из нашей БД)
        //1 параметр метода query() - имя таблицы,2 - масиив столбцов,значение которых мы хотим извлечь
       /* 3 - id,по которому мы будем извлекать данные из таблицы,пишем следующую запись:Util.KEY_ID + "=?"
        С помощью этой записи мы запрашиваем из таблицы запись по столбцу Util.KEY_ID*/
        //4 - String массив,указываем id из параметра метода,String.valueOf() - метод,который приводит к типу int
        //5,6,7,8 - null(разберемя с нмими позже)

        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.KEY_ID, Util.TABLE_NAME, Util.KEY_PRICE},
                Util.KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null,
                null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        //Создаем новый обЪект класса Car и используем конструктор с 3 параметрами
        //Здесь (ниже) мы указываем индексы наших столбцов
        //Нумерация индексов начинается с 0,поэтому столбец id будет иметь индекс 0 ,name - 1,price -2
        //Так же (ниже) мы получаем ранне вписанные данные с помощью метода .getString(),индекс - определенный столбец
        Car car = new Car(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        return car;
    }

    //Создадим ниже метод,с помощью которого мы будем извлекать все записи из нешей таблицы
    //Данный метод будет возвращать не 1 машину ,а несколько,поэтому будем использовать класс List<E>
    //Классы,которые имеют вид списка наследуются от List<E>,например ArrayList<E>,и мы легко можем возвращать например ArrayList<E>,т.к этот класс наследуется от List<E>

    public List getAllCars(){
        //Также создаем  обЪект класса  SQLiteDatabase,будем считывать данные,поэтому используем метод this.getReadableDatabase(
        SQLiteDatabase db = this.getReadableDatabase();
        //Cjplftv обЪект класса List<Car> типа Car и присваиваем класс ArrayList<>(),т.к он наследуется от List<E>
        List<Car> carsList = new ArrayList<>();
        //Ниже код на SQL
        //Пишем SELECT * FROM,* - значит,что выбираем все из таблицы с именем Util.TABLE_NAME

        String selectAllCars = "SELECT * FROM " + Util.TABLE_NAME;
        //С помощью класса Cursor и метода db.rawQuery() будем извлекать данные
        //1 параметр - наша строка с SQL данными,2 - null
        Cursor cursor = db.rawQuery(selectAllCars,null);
        //Метод cursor.moveToFirst() возвращает true,если таблица имеет данные,else - false
        //Напишем цикл do while(),с помощбю которого мы будем извлекать данные из таблицы до тех пор,пока там есть данные
        //cursor.moveToNext() - идет считывание данных пока есть следующая строка
        if (cursor.moveToFirst()){
            do {
                //Пока в таблице есть данные создаем обЪект класса Car и присваиваем id,name,price, с помощью setterов
                Car car = new Car();
                car.setId(Integer.parseInt(cursor.getString(0)));
                car.setName(cursor.getString(1));
                car.setPrice(cursor.getString(2));
                //Добавляем в наш лист созданный Car
                carsList.add(car);
            } while (cursor.moveToNext());
        }
        //Возвращаем наш List
        return carsList;
    }
    //Создадим метод,с пмощью которого мы будем обновлять наши машины
    public int updateCar(Car car){
        //Будем записывать данные,поэтому используем this.getWritableDatabase()
        SQLiteDatabase db = this.getWritableDatabase();
        //Здесь нам поможет класс Content Values
        ContentValues cv = new ContentValues();
        cv.put(Util.KEY_NAME,car.getName());
        cv.put(Util.KEY_PRICE,car.getPrice());
        //Обновляем нашу БД
        //1 параметр - имя таблицы,2 - ContentValues,3 куда помещать,т.к мы находим по id,то указываем Util.KEY_ID + "=?"
        //4 - id,который мы извлечем из обЪекта класса Car,который мы передавали в параметрах метода
        return db.update(Util.TABLE_NAME,cv,Util.KEY_ID  + "=?",new String[]{String.valueOf(car.getId())});

    }
        //Последняя опреция CRUD - delete
        //Создадим метод,который будет удалять нашу машину
    public void deleteCar(Car car){
        SQLiteDatabase db = this.getWritableDatabase();
        //В метод delete() помещаем теже самые параметры,что и в updateCar(),кроме ContentValues
        db.delete(Util.TABLE_NAME,Util.KEY_ID  + "=?",new String[]{String.valueOf(car.getId())});
        //После мы должны разорвать соединение с БД,с помощью метода close()
        db.close();
    }
}