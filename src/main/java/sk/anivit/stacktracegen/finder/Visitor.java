package sk.anivit.stacktracegen.finder;
public interface Visitor<T> {
    /**
     * @return {@code true} if the algorithm should visit more results,
     * {@code false} if it should terminate now.
     */
    public boolean visit(T t);
}