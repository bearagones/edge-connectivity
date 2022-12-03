import java.util.HashMap;

public class Partition {

    HashMap<Vertex, Cluster> clusterMap = new HashMap<>();

    public Partition() {
    }

    public Cluster find(Cluster c) {
        return c.head;
    }

    public boolean isSameCluster(Vertex u, Vertex v) {
        return find(clusterMap.get(u)) == find(clusterMap.get(v));
    }

    public void union(Vertex c1, Vertex c2) {
        Cluster a = find(clusterMap.get(c1));
        Cluster b = find(clusterMap.get(c2));

        if (a.size > b.size) {
            a.tail.next = b;
            Cluster temp = b;

            while (temp != null) {
                temp.head = a;
                temp = temp.next;
            }
            a.tail = b.tail;
            a.size += b.size;

        } else {
            b.tail.next = a;
            Cluster temp = a;

            while (temp != null) {
                temp.head = b;
                temp = temp.next;
            }
            b.tail = a.tail;
            b.size += a.size;
        }
    }

    public Cluster makeCluster(Vertex v) {
        Cluster c = new Cluster();
        clusterMap.put(v, c);
        return c;
    }
}

class Cluster {

    Cluster next;
    Cluster head;
    Cluster tail;
    int size;

    public Cluster() {
        next = null;
        head = this;
        tail = head;
        size = 1;
    }
}
