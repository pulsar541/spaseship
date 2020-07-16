package applib;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class ButtonImage {

    private Rectangle rect;
    private Texture texture;

    public ButtonImage() {
    }

    public ButtonImage(String internalImagePath, float centerX, float centerY) {
        texture = new Texture(internalImagePath);
        rect = new Rectangle(   centerX - texture.getWidth()/2, centerY - texture.getHeight()/2,   texture.getWidth(),     texture.getHeight());
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
