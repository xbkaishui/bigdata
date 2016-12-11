package es;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/client.html
 * <p/>
 * Created by xbkaishui on 16/12/11.
 */
public class EsTest {
    private static final Gson gson = new GsonBuilder().create();

    //    elasticsearch test
    public static void main(String[] args) throws Exception {
        //        testIndex();
        testBulkIndex();
    }

    private static void testBulkIndex() throws Exception {
        TransportClient client = initClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        Map<String, Object> json = new HashMap<String, Object>();
        json.put("user", "kimchy3");
        json.put("postDate", new Date());
        json.put("message", "trying out Elasticsearch 2");

        // either use client#prepare, or use Requests# to directly build index/delete requests
        bulkRequest.add(client.prepareIndex("twitter", "tweet", null).setSource(json));

        json.put("user", "kimchy4");
        bulkRequest.add(client.prepareIndex("twitter", "tweet", null).setSource(json));

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            System.out.printf(bulkResponse.buildFailureMessage());
            // process failures by iterating through each bulk response item
        }

    }

    private static void testIndex() throws Exception {
        //        Settings settings = Settings.builder()
        //            .put("cluster.name", "myClusterName").build();
        TransportClient client = initClient();

        List<DiscoveryNode> nodes = client.listedNodes();
        System.out.println(nodes);

        Map<String, Object> json = new HashMap<String, Object>();
        json.put("user", "kimchy2");
        json.put("postDate", new Date());
        json.put("message", "trying out Elasticsearch");

        IndexResponse response = client.prepareIndex("twitter", "tweet").setSource(json).get();
        System.out.println(response.toString());
    }

    private static TransportClient initClient() throws UnknownHostException {
        Settings settings = Settings.builder().put("client.transport.sniff", true).build();
        return new PreBuiltTransportClient(settings).addTransportAddress(
            new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }
}
