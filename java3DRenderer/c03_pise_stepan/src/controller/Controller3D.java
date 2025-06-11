package controller;

import raster.ZBuffer;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.TriangleRasterizer;
import render.Renderer;
import shader.*;
import solid.*;
import transforms.Mat4;
import transforms.Mat4OrthoRH;
import transforms.Mat4PerspRH;
import transforms.Mat4RotX;
import transforms.Mat4RotY;
import transforms.Mat4Scale;
import transforms.Mat4Transl;
import transforms.Vec3D;
import view.Panel;
import transforms.Camera;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Controller3D {
    private final Panel panel;
    private final ZBuffer zBuffer;
    private final LineRasterizer lineRasterizer;
    private final TriangleRasterizer triangleRasterizer;
    private final Renderer renderer;

    private Camera camera;
    private Mat4 proj;
    private int mouseX, mouseY, azimuth, zenith;
    private boolean isProjection;
    private int shaderMode = 0;
    private boolean scalingMode = false;
    private boolean translationMode = false;
    private boolean rotationMode = false;
    public boolean rotX;
    private Solid selectedObject;
    private Timer rotationTimer;
    private List<Solid> Solids;

    public Controller3D(Panel panel) {

        this.panel = panel;
        this.zBuffer = new ZBuffer(panel.getRaster());
        // TODO: pozor, sem bude vstupovat zBuffer
        this.lineRasterizer = new LineRasterizerGraphics(panel.getRaster());
        lineRasterizer.setColor(0xffffff);
        this.triangleRasterizer = new TriangleRasterizer(zBuffer, lineRasterizer, new ShaderInterpolated());
        this.renderer = new Renderer(lineRasterizer, triangleRasterizer, panel.getWidth(), panel.getHeight(), null,
                proj);

        Solids = new ArrayList<>();
        initListeners();
        initObjects();
        redraw();
        startRotation();
    }

    public void initObjects() {
        initCamera();

        Solids.clear();
        Solids.add(new Pyramid());
        Solids.add(new Arrow());
        Solids.add(new Cube());
        Solids.add(new Pyramid());

        selectedObject = Solids.get(0);

        translateObject(Solids.get(2), 2, 0, 0);
        translateObject(Solids.get(3), 0.5f, 0, 0);

    }

    public void initCamera() {
        camera = new Camera(new Vec3D(0, 0, 0), Math.PI + 1, Math.PI * -0.125, 5, false);
        proj = new Mat4PerspRH(
                Math.PI / 4,
                panel.getHeight() / (double) panel.getWidth(),
                0.1,
                20);

    }

    private void initListeners() {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_C:
                        initObjects();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                    case KeyEvent.VK_1:
                        if (Solids.get(0) != null) {
                            selectedObject = Solids.get(0);
                        }
                        break;
                    case KeyEvent.VK_2:
                        if (Solids.get(2) != null) {
                            selectedObject = Solids.get(2);
                        }
                        break;
                    case KeyEvent.VK_3:
                        if (Solids.get(2) != null) {
                            selectedObject = Solids.get(2);
                        }
                        break;
                    case KeyEvent.VK_4:
                        if (Solids.get(3) != null) {
                            selectedObject = Solids.get(3);
                        }
                        break;
                    case KeyEvent.VK_P:
                        if (isProjection) {
                            isProjection = !isProjection;
                            proj = new Mat4PerspRH(
                                    Math.PI / 4,
                                    panel.getHeight() / (double) panel.getWidth(),
                                    0.1,
                                    20);
                        } else {
                            isProjection = !isProjection;

                            proj = new Mat4OrthoRH(panel.getHeight() / (double) panel.getWidth() * 4, 4, 0.1, 20);
                        }
                        break;
                    case KeyEvent.VK_E:
                        scalingMode = true;
                        break;
                    case KeyEvent.VK_T:
                        translationMode = true;
                        break;
                    case KeyEvent.VK_R:
                        rotationMode = true;
                        break;
                    case KeyEvent.VK_O:
                        shaderMode = (shaderMode + 1) % 3;

                        if (shaderMode == 0) {
                            triangleRasterizer.setShader(new ShaderInterpolated());

                        } else if (shaderMode == 1) {
                            triangleRasterizer.setShader(new ShaderConst());

                        } else {
                            triangleRasterizer.setShader(null);
                        }
                        break;
                }

                if (e.getKeyCode() == KeyEvent.VK_W) {
                    camera = camera.forward(0.1);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    camera = camera.backward(0.1);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    camera = camera.left(0.1);
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    camera = camera.right(0.1);
                }

                if (selectedObject != null) {
                    if (scalingMode) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            scaleObject(selectedObject, 1.1);
                        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            scaleObject(selectedObject, 0.9);
                        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            scaleObject(selectedObject, 1.1);
                        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            scaleObject(selectedObject, 0.9);
                        }
                    }
                    if (translationMode) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            translateObject(selectedObject, 0, 0.1, 0);
                        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            translateObject(selectedObject, 0, -0.1, 0);
                        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            translateObject(selectedObject, -0.1, 0, 0);
                        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            translateObject(selectedObject, 0.1, 0, 0);

                        }
                    }
                    if (rotationMode) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            rotX = true;
                            rotateObject(selectedObject, 5);
                        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            rotX = true;
                            rotateObject(selectedObject, -5);
                        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            rotX = false;
                            rotateObject(selectedObject, 5);
                        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            rotX = false;
                            rotateObject(selectedObject, -5);
                        }
                    }
                }
                redraw();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    scalingMode = false;
                } else if (e.getKeyCode() == KeyEvent.VK_T) {
                    translationMode = false;
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    rotationMode = false;
                }

            }
        });
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    redraw();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    redraw();
                }
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent ev) {
                if (SwingUtilities.isLeftMouseButton(ev)) {
                    redraw();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x1 = e.getX();
                int y1 = e.getY();
                if (x1 < mouseX)
                    azimuth += 1;
                if (x1 > mouseX)
                    azimuth -= 1;
                if (y1 < mouseY)
                    zenith += 1;
                if (y1 > mouseY)
                    zenith -= 1;

                if (zenith < -90) {
                    zenith = -90;
                }
                if (zenith > 90) {
                    zenith = 90;
                }

                azimuth %= 360;
                camera = camera.withAzimuth(Math.toRadians(azimuth));
                camera = camera.withZenith(Math.toRadians(zenith));
                mouseX = e.getX();
                mouseY = e.getY();
                redraw();
            }
        });
    }

    private void redraw() {
        panel.clear();
        renderer.setProj(proj);
        renderer.setView(camera.getViewMatrix());

        for (Solid solid : Solids) {
            renderer.renderSolid(solid);
        }

        panel.repaint();
        zBuffer.cleanZbuff();

    }

    public void startRotation() {
        rotationTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Solids.get(2) != null) {
                    rotateObject(Solids.get(2), 5);
                    redraw();
                }
            }
        });
        rotationTimer.start();
    }

    private void scaleObject(Solid object, double scaleFactor) {
        Mat4 scale = new Mat4Scale(scaleFactor, scaleFactor, scaleFactor);
        object.setModel(scale.mul(object.getModel()));
    }

    private void translateObject(Solid object, double dx, double dy, double dz) {
        Mat4 translation = new Mat4Transl(dx, dy, dz);
        object.setModel(object.getModel().mul(translation));
    }

    private void rotateObject(Solid object, double angle) {

        Mat4 rotation = null;

        if (rotX == true) {
            rotation = new Mat4RotX(Math.toRadians(angle));

        } else {
            rotation = new Mat4RotY(Math.toRadians(angle));
        }

        object.setModel(rotation.mul(object.getModel()));
    }

}
