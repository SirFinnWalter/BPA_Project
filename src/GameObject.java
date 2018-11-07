public interface GameObject {
    void render(RenderHandler renderer, int xZoom, int yZoom);

    void update(BombGame game);
}