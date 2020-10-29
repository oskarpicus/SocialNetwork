package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.repository.Repository;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Community {

    private final Repository<Tuple<Long,Long>, Friendship> repository;

    public Community(Repository<Tuple<Long, Long>, Friendship> repository) {
        this.repository = repository;
    }

    public int getNumberOfCommunities(){
        Iterable<Friendship> list = repository.findAll();


        Map<Long,List<Long>> listMap = new HashMap<>();

        for(Friendship friendship : list){
            Long id1 = friendship.getId().getLeft();
            Long id2 = friendship.getId().getRight();
            if(listMap.get(id1)==null){
                ArrayList<Long> arrayList = new ArrayList<>();
                arrayList.add(id2);
                listMap.put(id1,arrayList);
            }
            else{
                listMap.get(id1).add(id2);
            }
            if(listMap.get(id2)==null){
                ArrayList<Long> arrayList = new ArrayList<>();
                arrayList.add(id1);
                listMap.put(id2,arrayList);
            }
            else{
                listMap.get(id2).add(id1);
            }
        }

        Stack<Long> stack = new Stack<>();
        int numberNodes = listMap.keySet().size();
        Map<Long,Boolean> visited = new HashMap<>();
        Map<Long,Long> parents = new HashMap<>();
        for(Long node : listMap.keySet()){
            visited.put(node,false);
            parents.put(node,null);
        }

        int contor = 0;

        stack.push((Long)listMap.keySet().toArray()[0]);
        while(!stack.empty()){
            Long u = stack.pop();
            if(!visited.get(u)){
                visited.replace(u,true);
                for(Long w : listMap.get(u)){
                  //  Long w = (tuple.getLeft().equals(u) ? tuple.getRight() : u ); //we take the adjacent node
                    if(visited.get(w))
                        continue;
                    stack.push(w);
                    parents.replace(w,u);
                }
            }
        }

        int nr=0;
        for(Long node : parents.values()){
            if(node==null)
                nr++;
        }
        return nr;
    }
}
