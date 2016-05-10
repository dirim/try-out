package com.saifproject.bluegape;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MYDBHandler extends SQLiteOpenHelper {

	public static final String dbname="Alumini";
	public static final int ver=1;
	ArrayList<String> imgvalue = new ArrayList<String>();
	ArrayList<String> about = new ArrayList<String>();
	int k=0;
	String data;

	public MYDBHandler(Context context, String name, CursorFactory factory,
					   int version) {
		super(context, dbname, factory, ver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String query="create table info (id integer primary key autoincrement, imgurl varchar(300) , about varchar(30));";
		db.execSQL(query);
		System.out.println("Table is made Successfully ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void add(String val)
	{
		ContentValues values=new ContentValues();
		int u=0;
		SQLiteDatabase db = getWritableDatabase();
			values.put("imgurl", val);
			values.put("about", "saif2");
			db.insert("info", null, values);
		db.close();
		System.out.println("Data Inserted Successfully");

	}
	
	public ArrayList<String> show(){

		SQLiteDatabase db=getWritableDatabase();
		String query="select * from info ;";
		System.out.println("I am after select");
		//Cursor point to a location is your result
		Cursor c=db.rawQuery(query, null);
		System.out.println("I am after cursor object");

		//Move to the first row
		c.moveToFirst();
		System.out.println("Move to first");

		while(!c.isAfterLast())
		{
			if(c.getString(c.getColumnIndex("about"))!=null)
			{

				imgvalue.add(c.getString(c.getColumnIndex("imgurl")));
				about.add(c.getString(c.getColumnIndex("about")));
				data=data+"\n"+c.getString(c.getColumnIndex("imgurl"));
//				dbString2 +="\n";
				c.moveToNext();
				System.out.println("Value of database is :"+imgvalue.get(k));
				k++;
			}
		}


		db.close();
		return imgvalue;
	}
	
	public void del()
	{
//		SQLiteDatabase sq=getWritableDatabase();
//		sq.execSQL("DELETE FROM location where imgurl like 'a%';");
//		System.out.println("Deleted Successfully..");
	}
		

}
