package com.flipkart.flap.thunderingrhino.utils;

import com.flipkart.flap.thunderingrhino.entities.CommonEntities;
import com.flipkart.flap.thunderingrhino.entities.NewDashboard;
import com.flipkart.flap.thunderingrhino.entities.ProductModules;
import com.flipkart.flap.thunderingrhino.entities.ThunderingDash;
import org.skife.jdbi.v2.sqlobject.*;

import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import javax.sql.CommonDataSource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by suchi.sethi on 21/08/15.
 */




@RegisterMapper(ThunderingDash.ThunderingDashMapper.class)
public interface  ThunderingDashDAO{

    @SqlQuery("select dash_key, url, display from dashboard_details where exist = 'TRUE' order by `display` asc")
    List<ThunderingDash> dashboardEntry();

    @SqlUpdate("insert into dashboard_details (dash_key, url, display) values (:dash_key, :url, :display)")
    void dashboardinsert(@Bind("dash_key") String dash_key, @Bind("url") String url, @Bind("display") String display );

    @SqlUpdate("update dashboard_details set exist='FALSE' where dash_key = :dash_key")
    void dashboarddelete(@Bind("dash_key") String dash_key);

    @Mapper(NewDashboard.NewDashboardMapper.class)
    @SqlQuery("select p.productname,pm.modulename,d.dash_key,d.url,d.display from products p left join product_modules pm on p.productid=pm.productid left join dashboard d on d.moduleid=pm.moduleid")
    List<NewDashboard> getNewEntry();

    @Mapper(CommonEntities.getProducts.class)
    @SqlQuery("select productid ,productname from products")
    Iterator<Map<Integer,String>> getProducts();

    @Mapper(ProductModules.ProductModulesMapper.class)
    @SqlQuery("select p.productid, moduleid,modulename  from products p left join product_modules m on m.productid=p.productid")
    List<ProductModules> getModules();

    @SqlUpdate(" insert into dashboard(dash_key,url,display,moduleid) values(:dash_key, :url, :display,:moduleid)")
    @GetGeneratedKeys
    int createDashboard(@Bind("dash_key") String dash_key, @Bind("url") String url, @Bind("display") String display,@Bind("moduleid") int moduleid);

    @SqlUpdate(" insert into product_modules(productid,modulename) values(:productid,:modulename)")
    @GetGeneratedKeys
    int createModuel(@Bind("productid") int productid, @Bind("modulename") String modulename);

    @SqlQuery("select dash_key from dashboard where dash_key = :dash_key")
    String getDash_key(@Bind("dash_key") String dash_key);

    @SqlUpdate("Delete from dashboard where dash_key = :dash_key")
    void deleteDashboard(@Bind("dash_key") String dash_key);

    @SqlQuery("select dash_key, url, display from dashboard order by `display` asc")
    List<ThunderingDash> newdashboardEntry();
}
