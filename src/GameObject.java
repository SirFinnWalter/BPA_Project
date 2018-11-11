public interface GameObject {
    public Collider getCollider();

    public void render(RenderHandler renderer, int xZoom, int yZoom);

    public void update(BombGame game);

    public void init(BombGame game);
}