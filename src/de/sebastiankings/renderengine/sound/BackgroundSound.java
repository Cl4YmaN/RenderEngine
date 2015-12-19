package de.sebastiankings.renderengine.sound;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
 
public class BackgroundSound {
  /** Buffers hold sound data. */
  IntBuffer buffer = BufferUtils.createIntBuffer(1);
 
  /** Sources are points emitting sound. */
  IntBuffer source = BufferUtils.createIntBuffer(1);
}