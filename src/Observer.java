public interface Observer {
    void update(Board board, FireResult result);
    
    // Metoda wywolywana przy cofaniu ruchu (undo)
    default void onUndo(Board board, FireResult originalResult) {
        // Domyslna implementacja - nic nie rob
    }
}