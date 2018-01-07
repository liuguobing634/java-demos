package lew.bing;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.mongodb.MongoClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;




public class MongoApp {

    private static final Log log = LogFactory.getLog(MongoApp.class);

    public static void main(String[] args) {
        MongoOperations mongoOps = new MongoTemplate(new MongoClient(),"test");
        mongoOps.insert(new Person("刘国兵",26));

    }

}
