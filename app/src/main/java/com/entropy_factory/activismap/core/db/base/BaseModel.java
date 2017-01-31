package com.entropy_factory.activismap.core.db.base;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by ander on 20/07/16.
 */
public class BaseModel extends Model {

    private static final String TAG = "BaseModel";

    protected <T extends Model, F extends Model> List<T> getManyThrough(Class<T> targetClass, Class<F> joinClass, String targetForeignKeyInJoin, String foreignKeyInJoin){
        return new Select()
                .from(targetClass)
                .as("target_model")
                .join(joinClass)
                .as("join_model")
                .on("join_model." + targetForeignKeyInJoin + " = " + "target_model.id")
                .where("join_model." + foreignKeyInJoin + " = " + this.getId())
                .execute();
    }
}
