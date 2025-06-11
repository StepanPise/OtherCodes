package shader;

import model.Vertex;
import transforms.Col;

public class ShaderConst implements Shader {

    @Override
    public Col getColor(Vertex pixel) {
        return new Col(0xff00ff);
    }
}
