package fi.mozarch;

import java.io.*;

public class Base64OutputStream extends FilterOutputStream
{  
	public Base64OutputStream(OutputStream out)
	{  super(out);
	}
	
	public final void writeBoolean(boolean v) throws IOException 	{	write(v ? 1 : 0);
    }
	
	public final void writeByte(int v) throws IOException 	{	write(v);
    }

    public final void writeShort(int v) throws IOException 	{	write((v >>> 8) & 0xFF);
		write((v >>> 0) & 0xFF);
    }

    public final void writeChar(int v) throws IOException 	{	write((v >>> 8) & 0xFF);
		write((v >>> 0) & 0xFF);
    }

    public final void writeInt(int v) throws IOException 	{	write((v >>> 24) & 0xFF);
		write((v >>> 16) & 0xFF);
		write((v >>>  8) & 0xFF);
		write((v >>>  0) & 0xFF);
    }
	
	public final void writeLong(long v) throws IOException 	{	write((int)(v >>> 56) & 0xFF);
		write((int)(v >>> 48) & 0xFF);
		write((int)(v >>> 40) & 0xFF);
		write((int)(v >>> 32) & 0xFF);
		write((int)(v >>> 24) & 0xFF);
		write((int)(v >>> 16) & 0xFF);
		write((int)(v >>>  8) & 0xFF);
		write((int)(v >>>  0) & 0xFF);
    }		public final void writeFloat(float v) throws IOException 	{	writeInt(Float.floatToIntBits(v));
    }		public final void writeDouble(double v) throws IOException 	{	writeLong(Double.doubleToLongBits(v));
    }


   public void write(int c) throws IOException
   {  inbuf[i] = c;
      i++;
      if (i == 3)
      {  super.write(toBase64[(inbuf[0] & 0xFC) >> 2]);
         super.write(toBase64[((inbuf[0] & 0x03) << 4) |
            ((inbuf[1] & 0xF0) >> 4)]);
         super.write(toBase64[((inbuf[1] & 0x0F) << 2) |
            ((inbuf[2] & 0xC0) >> 6)]);
         super.write(toBase64[inbuf[2] & 0x3F]);
         col += 4;
         i = 0;
         if (col >= 76)
         {  //super.write('\n');
            col = 0;
         }
      }
   }

   public void flush() throws IOException
   {  if (i == 1)
      {  super.write(toBase64[(inbuf[0] & 0xFC) >> 2]);
         super.write(toBase64[(inbuf[0] & 0x03) << 4]);
         super.write('=');
         super.write('=');
      }
      else if (i == 2)
      {  super.write(toBase64[(inbuf[0] & 0xFC) >> 2]);
         super.write(toBase64[((inbuf[0] & 0x03) << 4) |
            ((inbuf[1] & 0xF0) >> 4)]);
         super.write(toBase64[(inbuf[1] & 0x0F) << 2]);
         super.write('=');
      }
      i = 0;
   }

   private static char[] toBase64 =
   {  'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
      'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
      'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
      'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
      'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
      'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
      'w', 'x', 'y', 'z', '0', '1', '2', '3',
      '4', '5', '6', '7', '8', '9', '+', '/'
   };

   private int col = 0;
   private int i = 0;
   private int[] inbuf = new int[3];
}
