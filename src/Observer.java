public interface Observer {
    void update(Board board, FireResult result);
    
    default void onUndo(Board board, FireResult originalResult) {
    }
}