import java.util.HashMap;

public class Partition {

    HashMap<Vertex, Cluster> clusterMap;

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
            Cluster iter = b;

            while (iter != null) {
                iter.head = a;
                iter = iter.next;
            }
            a.tail = b.tail;
            a.size += b.size;

        } else {
            b.tail.next = a;
            Cluster iter = a;

            while (iter != null) {
                iter.head = b;
                iter = iter.next;
            }
            b.tail = a.tail;
            b.size += a.size;
        }
    }

    public void makeCluster(Vertex v) {
        Cluster c = new Cluster();
        clusterMap.put(v, c);
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
        size = 1;
    }
}
