public interface Element<T> {
    void accept(Visitor<T> visitor);
}
