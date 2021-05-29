package applib;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class ButtonImage {

    private Rectangle rect;
    private Texture texture;
    private float _scale = 1.0f;

    public ButtonImage() {
    }

    public ButtonImage(String internalImagePath, float centerX, float centerY) {
        texture = new Texture(internalImagePath);
        rect = new Rectangle(   centerX - texture.getWidth()/2, centerY - texture.getHeight()/2,   texture.getWidth(),     texture.getHeight());
        _scale = 1;
    }

    public ButtonImage(String internalImagePath, float centerX, float centerY, float scale) {
        texture = new Texture(internalImagePath);
        _scale = scale;
        rect = new Rectangle(   centerX - scale*texture.getWidth()/2, centerY - scale*texture.getHeight()/2,   scale*texture.getWidth(),     scale*texture.getHeight());

    }


    public boolean isTouched(Vector3 touchPos) {
       return  (rect.overlaps(new Rectangle(touchPos.x , touchPos.y , 1, 1)));
    }

    public void draw(SpriteBatch sb) {
        sb.draw(texture,
                rect.x,
                rect.y,
                rect.width,
                rect.height);
    }

    public void dispose() {
        if(texture != null)
            texture.dispose();
    }

}
