package socialnetwork.service;

import socialnetwork.domain.User;
import socialnetwork.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Community {
    private final Repository<Long, User> repositoryUsers;

    public Community(Repository<Long, User> repositoryUsers) {
        this.repositoryUsers = repositoryUsers;
    }

    /**
     * Method for creating the adjacency list of the users and their friendships
     * @return list : List<User>
     */
    private List<User> getAdjacencyList(){
        Iterable<User> all = this.repositoryUsers.findAll();
        return StreamSupport.stream(all.spliterator(),false).collect(Collectors.toList());
    }

    /**
     * Method for obtaining the number of communities in the social network
     * @return nr : int, represents the total number of communities
     */
    public int getNumberOfCommunities() {
        Map<User, Boolean> visited = new HashMap<>();
        Map<User, User> parents = new HashMap<>();

        List<User> adjacencyList = getAdjacencyList();

        for (User node : adjacencyList) {
            visited.put(node, false);
            parents.put(node, null);
        }

        for (User node : adjacencyList) {
            if (!visited.get(node)) {
                DFS_visit(node, visited, parents);
            }
        }

        int result = 0;
        for (User parent : parents.values()) {
            if (parent == null)
                result++;
        }
        return result;
    }

    /**
     * Method for visiting a node in a Depth First fashion
     * @param node : User, the node to be visited
     * @param visited : Map<User,Long>, for keeping track if a node was visited or not
     * @param parents : Map<User,Long>, for keeping track of each node's parent
     */
    private void DFS_visit(User node, Map<User, Boolean> visited, Map<User, User> parents) {
        visited.replace(node, true);

        for (User adjacentNode : node.getFriends()) {
            if (!visited.get(adjacentNode)) {
                parents.put(adjacentNode, node);
                DFS_visit(adjacentNode, visited, parents);
            }
        }
    }
}




//public class Community {
//
//    private final Repository<Tuple<Long,Long>, Friendship> repository;
//
//    public Community(Repository<Tuple<Long, Long>, Friendship> repository) {
//        this.repository = repository;
//    }
//
//    /**
//     * Method for creating the adjacency list of the users and their friendships
//     * @return listMap : Map<Long,List<Long>>, where
//     *      key - an id of a user
//     *      value - list of all users' id which are friends with key
//     */
//    private Map<Long,List<Long>> getAdjacencyList(){
//        Map<Long,List<Long>> listMap = new HashMap<>();
//        Iterable<Friendship> allFriendships = repository.findAll();
//
//        for(Friendship friendship : allFriendships){
//            Long id1 = friendship.getId().getLeft();
//            Long id2 = friendship.getId().getRight();
//            if(listMap.get(id1)==null){
//                ArrayList<Long> arrayList = new ArrayList<>();
//                arrayList.add(id2);
//                listMap.put(id1,arrayList);
//            }
//            else{
//                listMap.get(id1).add(id2);
//            }
//            if(listMap.get(id2)==null){
//                ArrayList<Long> arrayList = new ArrayList<>();
//                arrayList.add(id1);
//                listMap.put(id2,arrayList);
//            }
//            else{
//                listMap.get(id2).add(id1);
//            }
//        }
//
//        return listMap;
//    }
//
//    /**
//     * Method for obtaining the number of communities in the social network
//     * @return nr : int, represents the total number of communities
//     */
//    public int getNumberOfCommunities(){
//
//        Map<Long,List<Long>> listMap = this.getAdjacencyList();
//
//        Map<Long,Boolean> visited = new HashMap<>();
//        Map<Long,Long> parents = new HashMap<>();
//        for(Long node : listMap.keySet()){
//            visited.put(node,false);
//            parents.put(node,null);
//        }
//
//        for(Long node : listMap.keySet()){
//            if(!visited.get(node)){
//                DFS_visit(node,listMap,visited,parents);
//            }
//        }
//
//        int result =0;
//        for(Long parent : parents.values()){
//            if(parent == null)
//                result++;
//        }
//        return result;
//    }
//
//    /**
//     * Method for visiting a node in a Depth First fashion
//     * @param node : Long, the node to be visited
//     * @param listMap : Map<Long,List<Long>>, the adjacency list
//     * @param visited : Map<Long,Long>, for keeping track is a node was visited or not
//     * @param parents : Map<Long,Long>, for keeping track of each node's parent
//     */
//    private void DFS_visit(Long node, Map<Long, List<Long>> listMap, Map<Long, Boolean> visited, Map<Long, Long> parents){
//        visited.replace(node,true);
//        for(Long adjacentNode : listMap.get(node)){
//            if(!visited.get(adjacentNode)){
//                parents.put(adjacentNode,node);
//                DFS_visit(adjacentNode,listMap,visited,parents);
//            }
//        }
//    }
//}
