public interface GameObject {

    // Called whenever possibles
    public void render(RenderHandler renderer, int xZoom, int yZoom);

    // Called ~ every 60 frames
    public void update(BombGame game);
}