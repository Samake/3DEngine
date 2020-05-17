package samake.engine.rendering;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL43;

import samake.engine.camera.Transformation;
import samake.engine.config.PropertiesHandler;
import samake.engine.logging.Console;
import samake.engine.logging.Console.LOGTYPE;
import samake.engine.rendering.buffer.ReflectionBuffer;
import samake.engine.rendering.buffer.RenderTarget;
import samake.engine.rendering.postprocess.PostProcess;
import samake.engine.rendering.renderer.CloudRenderer;
import samake.engine.rendering.renderer.ColorRenderer;
import samake.engine.rendering.renderer.EntityRenderer;
import samake.engine.rendering.renderer.SkyRenderer;
import samake.engine.rendering.renderer.TerrainRenderer;
import samake.engine.rendering.renderer.WaterRenderer;
import samake.engine.rendering.shadowmapping.ShadowMap;
import samake.engine.scene.Scene;

public class Renderer {
	
	public enum RENDERMODE {
		DEFAULT, DEBUG, WIREFRAME, DIFFUSE, NORMALS, ALBEDO, DEPTH, POSITION, COLOR
	}
	
	private Vector4f clipPlane;
	private Scene scene;
	private Transformation transformation;
	
	private ReflectionBuffer reflectionBuffer;
	
	private SkyRenderer skyRenderer;
	private TerrainRenderer terrainRenderer;
	private EntityRenderer entityRenderer;
	private CloudRenderer cloudRenderer;
	private WaterRenderer waterRenderer;
	private ColorRenderer colorRenderer;
	private ShadowMap shadowMap;
	
	private PostProcess postProcess;
	private RenderTarget multiSampledRenderTarget;
	private RenderTarget sceneRenderTarget;
	private RenderTarget brightnessRenderTarget;
	
	private int renderMode = 0;

	public Renderer() throws Exception {
		Console.print("Initialize Renderer...", LOGTYPE.OUTPUT, true);
		
		setTransformation(new Transformation());
		
		setReflectionBuffer(new ReflectionBuffer(PropertiesHandler.getWindowWidth(), PropertiesHandler.getWindowHeight(), 1));
		
		setMultiSampledRenderTarget(new RenderTarget(PropertiesHandler.getWindowWidth(), PropertiesHandler.getWindowHeight()));
		setSceneRenderTarget(new RenderTarget(PropertiesHandler.getWindowWidth(), PropertiesHandler.getWindowHeight(), RenderTarget.DEPTH_TEXTURE));
		setBrightnessRenderTarget(new RenderTarget(PropertiesHandler.getWindowWidth(), PropertiesHandler.getWindowHeight(), RenderTarget.DEPTH_TEXTURE));
		
		setSkyRenderer(new SkyRenderer());
		setTerrainRenderer(new TerrainRenderer());
		setEntityRenderer(new EntityRenderer());
		setCloudRenderer(new CloudRenderer());
		setWaterRenderer(new WaterRenderer());
		setColorRenderer(new ColorRenderer());
		setShadowMap(new ShadowMap());
		setPostProcess(new PostProcess());
		
		Console.print("Renderer started!", LOGTYPE.OUTPUT, true);
	}
	
	public void update() {
		if (transformation != null) {
			transformation.update();
		}
		
		skyRenderer.update();
		terrainRenderer.update();
		entityRenderer.update();
		waterRenderer.update();
		colorRenderer.update();
		shadowMap.update();
		postProcess.update();
    }
    
    public void render() {
    	if (scene != null) {
    		renderShadowMap();
        	renderBufferTextures();
        	
        	multiSampledRenderTarget.bindBuffer();
        	
        	renderScene();
        	renderWater();
        	
        	multiSampledRenderTarget.unbindBuffer();
        	multiSampledRenderTarget.drawBuffer(GL43.GL_COLOR_ATTACHMENT0, sceneRenderTarget);
        	multiSampledRenderTarget.drawBuffer(GL43.GL_COLOR_ATTACHMENT1, brightnessRenderTarget);
        	
        	postProcess.render(sceneRenderTarget.getMainTexture(), brightnessRenderTarget.getMainTexture());
        	
        	if (getRenderMode() == 1) {
            	colorRenderer.render(scene.getCamera(), transformation, scene);
        	}
        	
        	endFrame();
		}
	}
    
    private void renderShadowMap() {
    	shadowMap.render();
    }
    
    private void renderBufferTextures() {
		float waterHeight = 0.0f;
		float distance = scene.getCamera().getPosition().y * 2.0f;

		reflectionBuffer.bindReflectionBuffer();
		scene.getCamera().getPosition().y -= distance;
		transformation.invert();
		clipPlane = new Vector4f(0.0f, 1.0f, 0.0f, waterHeight + 0.25f);
		
		renderScene();
		
		reflectionBuffer.bindRefractionBuffer();
		scene.getCamera().getPosition().y += distance;
		transformation.invert();
		
		if (distance > waterHeight) {
			clipPlane = new Vector4f(0.0f, -1.0f, 0.0f, waterHeight + 0.25f);
		} else {
			clipPlane = new Vector4f(0.0f, 1.0f, 0.0f, waterHeight + 0.5f);
		}
		
		renderScene();
		
    	reflectionBuffer.unbind();
		
		clipPlane = new Vector4f(0.0f, -1.0f, 0.0f, 10000.0f);
	}
    
    public void startFrame() {
    	Vector3f fogColor = scene.getEnvironment().getAmbientColor();
    	
    	GL43.glClearColor(fogColor.x, fogColor.y, fogColor.z, 0.0f);
		GL43.glClearDepth(1.0);
		GL43.glClear(GL43.GL_COLOR_BUFFER_BIT | GL43.GL_DEPTH_BUFFER_BIT | GL43.GL_STENCIL_BUFFER_BIT);	
		GL43.glEnable(GL43.GL_DEPTH_TEST);
		GL43.glEnable(GL43.GL_STENCIL_TEST); 
		GL43.glEnable(GL43.GL_TEXTURE_2D);
		GL43.glEnable(GL43.GL_TEXTURE_3D);
		GL43.glEnable(GL43.GL_FRAMEBUFFER_SRGB);
		GL43.glEnable(GL43.GL_BLEND);
		GL43.glBlendFunc(GL43.GL_SRC_ALPHA, GL43.GL_ONE_MINUS_SRC_ALPHA); 
	}
    
    private void renderScene() {
		startFrame();
		
    	renderSky();
    	renderClouds();
    	renderTerrain();
    	renderEntities();
    }

	private void renderSky() {
		GL43.glDisable(GL43.GL_CULL_FACE);
		GL43.glPolygonMode(GL43.GL_FRONT_AND_BACK, GL43.GL_FILL);
		
		skyRenderer.render(scene.getCamera(), transformation, scene, clipPlane);
	}
	
	private void renderTerrain() {
    	GL43.glEnable(GL43.GL_CULL_FACE);
    	GL43.glCullFace(GL43.GL_BACK);
    	
		if (getRenderMode() == 2) {
			GL43.glPolygonMode(GL43.GL_FRONT_AND_BACK, GL43.GL_LINE);
		} else {
			GL43.glPolygonMode(GL43.GL_FRONT_AND_BACK, GL43.GL_FILL);
		}

		terrainRenderer.render(scene.getCamera(), transformation, scene, getRenderMode(), clipPlane);
		
		GL43.glDisable(GL43.GL_CULL_FACE);
	}

	private void renderEntities() {
    	GL43.glEnable(GL43.GL_CULL_FACE);
    	GL43.glCullFace(GL43.GL_BACK);
    	
		if (getRenderMode() == 2) {
			GL43.glPolygonMode(GL43.GL_FRONT_AND_BACK, GL43.GL_LINE);
		} else {
			GL43.glPolygonMode(GL43.GL_FRONT_AND_BACK, GL43.GL_FILL);
		}

		entityRenderer.render(scene.getCamera(), transformation, scene, getRenderMode(), clipPlane);
		
		GL43.glDisable(GL43.GL_CULL_FACE);
	}
	
	private void renderClouds() { 
    	GL43.glEnable(GL43.GL_CULL_FACE);
    	GL43.glCullFace(GL43.GL_BACK);

		if (getRenderMode() == 2) {
			GL43.glPolygonMode(GL43.GL_BACK, GL43.GL_LINE);
		} else {
			GL43.glPolygonMode(GL43.GL_BACK, GL43.GL_FILL);
		}

		cloudRenderer.render(scene.getCamera(), transformation, scene, renderMode, clipPlane);
		
		GL43.glDisable(GL43.GL_CULL_FACE);
	}
	
	private void renderWater() {
		GL43.glDisable(GL43.GL_CULL_FACE);
    	
		if (getRenderMode() == 2) {
			GL43.glPolygonMode(GL43.GL_FRONT_AND_BACK, GL43.GL_LINE);
		} else {
			GL43.glPolygonMode(GL43.GL_FRONT_AND_BACK, GL43.GL_FILL);
		}
		
		waterRenderer.render(reflectionBuffer, scene.getCamera(), transformation, scene, getRenderMode());
	}
	
    public void endFrame() {
		GL43.glDisable(GL43.GL_CULL_FACE);
		GL43.glDisable(GL43.GL_DEPTH_TEST);	
		GL43.glDisable(GL43.GL_STENCIL_TEST); 
		GL43.glDisable(GL43.GL_TEXTURE_2D);
		GL43.glDisable(GL43.GL_TEXTURE_3D);
		GL43.glDisable(GL43.GL_BLEND);
		GL43.glDisable(GL43.GL_FRAMEBUFFER_SRGB);
	}

	public void guiRender() {
		
	}
	
	public void changeRenderMode(RENDERMODE mode) {
		switch (mode) {
			case DEFAULT :
				setRenderMode(0);
				Console.print("Changed rendermode to: DEFAULT", LOGTYPE.OUTPUT, true);
				break;
			case DEBUG :
				setRenderMode(1);
				Console.print("Changed rendermode to: DEBUG", LOGTYPE.OUTPUT, true);
				break;
			case WIREFRAME :
				setRenderMode(2);
				Console.print("Changed rendermode to: WIREFRAME", LOGTYPE.OUTPUT, true);
				break;
			case DIFFUSE :
				setRenderMode(3);
				Console.print("Changed rendermode to: DIFFUSE", LOGTYPE.OUTPUT, true);
				break;
			case NORMALS :
				setRenderMode(4);
				Console.print("Changed rendermode to: NORMALS", LOGTYPE.OUTPUT, true);
				break;
			case ALBEDO :
				setRenderMode(5);
				Console.print("Changed rendermode to: ALBEDO", LOGTYPE.OUTPUT, true);
				break;
			case DEPTH :
				setRenderMode(6);
				Console.print("Changed rendermode to: DEPTH", LOGTYPE.OUTPUT, true);
				break;
			case POSITION :
				setRenderMode(7);
				Console.print("Changed rendermode to: POSITION", LOGTYPE.OUTPUT, true);
				break;
			case COLOR :
				setRenderMode(8);
				Console.print("Changed rendermode to: COLOR", LOGTYPE.OUTPUT, true);
				break;
		default:
			setRenderMode(0);
			Console.print("Changed rendermode to: DEFAULT", LOGTYPE.OUTPUT, true);
			break;
		}
	}
	
	public Transformation getTransformation() {
		return transformation;
	}

	public void setTransformation(Transformation transformation) {
		this.transformation = transformation;
	}
	
	public ReflectionBuffer getReflectionBuffer() {
		return reflectionBuffer;
	}

	public void setReflectionBuffer(ReflectionBuffer waterFrameBuffer) {
		this.reflectionBuffer = waterFrameBuffer;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public SkyRenderer getSkyRenderer() {
		return skyRenderer;
	}

	public void setSkyRenderer(SkyRenderer skyRenderer) {
		this.skyRenderer = skyRenderer;
	}

	public TerrainRenderer getTerrainRenderer() {
		return terrainRenderer;
	}

	public void setTerrainRenderer(TerrainRenderer terrainRenderer) {
		this.terrainRenderer = terrainRenderer;
	}

	public EntityRenderer getEntityRenderer() {
		return entityRenderer;
	}

	public void setEntityRenderer(EntityRenderer entityRenderer) {
		this.entityRenderer = entityRenderer;
	}

	public CloudRenderer getCloudRenderer() {
		return cloudRenderer;
	}

	public void setCloudRenderer(CloudRenderer cloudRenderer) {
		this.cloudRenderer = cloudRenderer;
	}

	public WaterRenderer getWaterRenderer() {
		return waterRenderer;
	}

	public void setWaterRenderer(WaterRenderer waterRenderer) {
		this.waterRenderer = waterRenderer;
	}

	public ColorRenderer getColorRenderer() {
		return colorRenderer;
	}

	public void setColorRenderer(ColorRenderer colorRenderer) {
		this.colorRenderer = colorRenderer;
	}

	public ShadowMap getShadowMap() {
		return shadowMap;
	}

	public void setShadowMap(ShadowMap shadowMap) {
		this.shadowMap = shadowMap;
	}

	public PostProcess getPostProcess() {
		return postProcess;
	}

	public void setPostProcess(PostProcess postProcess) {
		this.postProcess = postProcess;
	}

	public RenderTarget getMultiSampledRenderTarget() {
		return multiSampledRenderTarget;
	}

	public void setMultiSampledRenderTarget(RenderTarget multiSampledRenderTarget) {
		this.multiSampledRenderTarget = multiSampledRenderTarget;
	}

	public RenderTarget getSceneRenderTarget() {
		return sceneRenderTarget;
	}

	public void setSceneRenderTarget(RenderTarget sceneRenderTarget) {
		this.sceneRenderTarget = sceneRenderTarget;
	}

	public RenderTarget getBrightnessRenderTarget() {
		return brightnessRenderTarget;
	}

	public void setBrightnessRenderTarget(RenderTarget brightnessRenderTarget) {
		this.brightnessRenderTarget = brightnessRenderTarget;
	}

	public int getRenderMode() {
		return renderMode;
	}

	public void setRenderMode(int renderMode) {
		this.renderMode = renderMode;
	}

	public void destroy() {
		reflectionBuffer.destroy();
		skyRenderer.destroy();
		terrainRenderer.destroy();
		entityRenderer.destroy();
		cloudRenderer.destroy();
		waterRenderer.destroy();
		colorRenderer.destroy();
		shadowMap.destroy();
		postProcess.destroy();
		multiSampledRenderTarget.destroy();
		sceneRenderTarget.destroy();
		brightnessRenderTarget.destroy();
		
		Console.print("Renderer stopped!", LOGTYPE.OUTPUT, true);
	}
}
