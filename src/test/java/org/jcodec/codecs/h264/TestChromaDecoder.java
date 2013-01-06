package org.jcodec.codecs.h264;

import static org.jcodec.common.model.ColorSpace.YUV420;

import org.jcodec.codecs.h264.decode.ChromaDecoder;
import org.jcodec.codecs.h264.decode.model.DecodedChroma;
import org.jcodec.codecs.h264.decode.model.NearbyPixels;
import org.jcodec.codecs.h264.io.model.CodedChroma;
import org.jcodec.codecs.h264.io.model.ResidualBlock;
import org.junit.Test;

public class TestChromaDecoder extends JAVCTestCase {
	
	@Test
	public void testMB41() throws Exception {
		// MB 41
		int[] expectedCb = new int[] {
			113, 114, 114, 115, 115, 116, 116, 117,
			114, 115, 115, 116, 116, 117, 117, 118,
			115, 115, 116, 116, 117, 117, 118, 118,
			116, 116, 117, 117, 118, 118, 119, 119,
			116, 117, 117, 118, 118, 119, 119, 120,
			117, 117, 118, 118, 119, 119, 120, 120,
			118, 118, 119, 119, 120, 120, 121, 121,
			118, 119, 119, 120, 120, 121, 121, 122
		};
		
		int[] expectedCr = new int[] {
			135, 134, 134, 133, 133, 132, 132, 131,
			135, 134, 134, 133, 133, 132, 131, 131,
			135, 134, 134, 133, 133, 132, 131, 131,
			135, 134, 134, 133, 132, 132, 131, 131,
			135, 134, 134, 133, 132, 132, 131, 131,
			135, 134, 133, 133, 132, 132, 131, 130,
			135, 134, 133, 133, 132, 132, 131, 130,
			134, 134, 133, 133, 132, 131, 131, 130
		};
		
		// MB 40
		int[] mb40Cb = new int[] {
			106, 104,  99,  96, 106, 109, 114, 116,
			109, 106, 101,  99, 106, 109, 114, 116,
			114, 111, 106, 104, 106, 109, 114, 116,
			116, 114, 109, 106, 106, 109, 114, 116,
			116, 116, 116, 116, 109, 112, 117, 119,
			116, 116, 116, 116, 109, 112, 117, 119,
			116, 116, 116, 116, 109, 112, 117, 119,
			116, 116, 116, 116, 109, 112, 117, 119
		};
		
		int[] mb40Cr = new int[] {
			143, 143, 143, 143, 137, 137, 137, 137,
			143, 143, 143, 143, 137, 137, 137, 137,
			143, 143, 143, 143, 137, 137, 137, 137,
			143, 143, 143, 143, 137, 137, 137, 137,
			142, 142, 142, 142, 138, 138, 138, 138,
			142, 142, 142, 142, 138, 138, 138, 138,
			142, 142, 142, 142, 138, 138, 138, 138,
			142, 142, 142, 142, 138, 138, 138, 138
		};

		// MB 30
		int[] mb30Cb = new int[] {
			118, 118, 118, 118, 119, 119, 119, 119,
			118, 118, 118, 118, 119, 119, 119, 119,
			118, 118, 118, 118, 119, 119, 119, 119,
			118, 118, 118, 118, 119, 119, 119, 119,
			118, 118, 118, 118, 119, 119, 119, 119,
			118, 118, 118, 118, 119, 119, 119, 119,
			118, 118, 118, 118, 119, 119, 119, 119,
			118, 118, 118, 118, 119, 119, 119, 119
		};
		int[] mb30Cr = new int[] {
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132
		};
		// MB 29
		int[] mb29Cb = new int[] {
			127, 127, 127, 127, 128, 125, 120, 117,
			127, 127, 127, 127, 128, 125, 120, 117,
			127, 127, 127, 127, 128, 125, 120, 117,
			127, 127, 127, 127, 128, 125, 120, 117,
			125, 125, 125, 125, 124, 124, 124, 124,
			120, 120, 120, 120, 122, 122, 122, 122,
			108, 108, 108, 108, 116, 116, 116, 116,
			102, 102, 102, 102, 113, 113, 113, 113
		};
		int[] mb29Cr = new int[] {
			130, 130, 130, 130, 130, 130, 130, 130,
			130, 130, 130, 130, 130, 130, 130, 130,
			130, 130, 130, 130, 130, 130, 130, 130,
			130, 130, 130, 130, 130, 130, 130, 130,
			130, 130, 130, 130, 130, 130, 130, 130,
			133, 133, 133, 133, 133, 133, 133, 133,
			138, 138, 138, 138, 138, 138, 138, 138,
			141, 141, 141, 141, 141, 141, 141, 141
		};
		
		ChromaDecoder decoder = new ChromaDecoder(new int[] { 0, 0 }, 8,
				YUV420);
		NearbyPixels.Plane nCb = new NearbyPixels.Plane(mb40Cb, mb30Cb, mb29Cb,
				null);
		NearbyPixels.Plane nCr = new NearbyPixels.Plane(mb40Cr, mb30Cr, mb29Cr,
				null);
		ResidualBlock cbDC = new ResidualBlock(new int[] { -1, 0, 0, 0 });
		ResidualBlock[] cbAC = new ResidualBlock[] { null, null, null, null };
		ResidualBlock crDC = new ResidualBlock(new int[] { -1, 0, 0, 0 });
		ResidualBlock[] crAC = new ResidualBlock[] { null, null, null, null };

		CodedChroma chroma = new CodedChroma(cbDC, cbAC, crDC, crAC, null, null);
		int qp = 26;
		int chromaMode = 3;
		DecodedChroma actual = decoder.decodeChromaIntra(chroma, chromaMode,
				qp, nCb, nCr);
		assertArrayEquals(actual.getCb(), expectedCb);
		assertArrayEquals(actual.getCr(), expectedCr);
	}
	
	@Test
	public void testMB58() throws Exception {
		// MB 41
		int[] expectedCb = new int[] {
			117, 117, 117, 117, 117, 117, 117, 117,
			117, 117, 117, 117, 117, 117, 117, 117,
			117, 117, 117, 117, 117, 117, 117, 117,
			117, 117, 117, 117, 117, 117, 117, 117,
			119, 119, 119, 119, 118, 118, 118, 118,
			119, 119, 119, 119, 118, 118, 118, 118,
			119, 119, 119, 119, 118, 118, 118, 118,
			119, 119, 119, 119, 118, 118, 118, 118
		};
		
		int[] expectedCr = new int[] {
			134, 136, 141, 143, 147, 145, 140, 138,
			134, 136, 141, 143, 147, 145, 140, 138,
			134, 136, 141, 143, 147, 145, 140, 138,
			134, 136, 141, 143, 147, 145, 140, 138,
			132, 132, 132, 132, 143, 143, 143, 143,
			132, 132, 132, 132, 143, 143, 143, 143,
			132, 132, 132, 132, 143, 143, 143, 143,
			132, 132, 132, 132, 143, 143, 143, 143
		};
		
		// MB 57
		int[] mb57Cb = new int[] {
			121, 121, 121, 121,121, 121, 121, 121,
			121, 121, 121, 121,121, 121, 121, 121,
			121, 121, 121, 121,121, 121, 121, 121,
			121, 121, 121, 121,121, 121, 121, 121,
			121, 121, 121, 121,121, 121, 121, 121,
			121, 121, 121, 121,121, 121, 121, 121,
			121, 121, 121, 121,121, 121, 121, 121,
			121, 121, 121, 121,121, 121, 121, 121
		};
		
		int[] mb57Cr = new int[] {
			130, 130, 130, 130, 130, 130, 130, 130,
			130, 130, 130, 130, 130, 130, 130, 130,
			130, 130, 130, 130, 130, 130, 130, 130,
			130, 130, 130, 130, 130, 130, 130, 130,
			130, 130, 130, 130, 130, 130, 130, 130,
			130, 130, 130, 130, 130, 130, 130, 130,
			130, 130, 130, 130, 130, 130, 130, 130,
			130, 130, 130, 130, 130, 130, 130, 130
		};

		// MB 47
		int[] mb47Cb = new int[] {
			114, 114, 114, 114, 114, 114, 114, 114,
			114, 114, 114, 114, 114, 114, 114, 114,
			114, 114, 114, 114, 114, 114, 114, 114,
			114, 114, 114, 114, 114, 114, 114, 114,
			117, 117, 117, 117, 119, 119, 119, 119,
			117, 117, 117, 117, 119, 119, 119, 119,
			117, 117, 117, 117, 119, 119, 119, 119,
			117, 117, 117, 117, 119, 119, 119, 119
		};
		int[] mb47Cr = new int[] {
			135, 135, 135, 135, 137, 137, 137, 137,
			135, 135, 135, 135, 137, 137, 137, 137,
			135, 135, 135, 135, 137, 137, 137, 137,
			135, 135, 135, 135, 137, 137, 137, 137,
			136, 136, 136, 136, 137, 137, 137, 137,
			136, 136, 136, 136, 137, 137, 137, 137,
			136, 136, 136, 136, 137, 137, 137, 137,
			136, 136, 136, 136, 137, 137, 137, 137
		};
		// MB 46
		int[] mb46Cb = new int[] {
			119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119,
			119, 119, 119, 119, 119, 119, 119, 119
		};
		int[] mb46Cr = new int[] {
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132,
			132, 132, 132, 132, 132, 132, 132, 132
		};
		
		ChromaDecoder decoder = new ChromaDecoder(new int[] { 0, 0 }, 8,
				YUV420);
		NearbyPixels.Plane nCb = new NearbyPixels.Plane(mb57Cb, mb47Cb, mb46Cb,
				null);
		NearbyPixels.Plane nCr = new NearbyPixels.Plane(mb57Cr, mb47Cr, mb46Cr,
				null);
		ResidualBlock cbDC = new ResidualBlock(new int[] { -1, 0, 0, 0 });
		ResidualBlock[] cbAC = new ResidualBlock[] {
				new ResidualBlock(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0 }),
				new ResidualBlock(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0 }),
				new ResidualBlock(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0 }),
				new ResidualBlock(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0 }) };
		ResidualBlock crDC = new ResidualBlock(new int[] { 3, -1, 0, 1 });
		ResidualBlock[] crAC = new ResidualBlock[] {
				new ResidualBlock(new int[] { -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0 }),
				new ResidualBlock(new int[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0 }),
				new ResidualBlock(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0 }),
				new ResidualBlock(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0 }) };

		CodedChroma chroma = new CodedChroma(cbDC, cbAC, crDC, crAC, null, null);
		int qp = 27;
		int chromaMode = 0;
		DecodedChroma actual = decoder.decodeChromaIntra(chroma, chromaMode,
				qp, nCb, nCr);
		assertArrayEquals(actual.getCb(), expectedCb);
		assertArrayEquals(actual.getCr(), expectedCr);
	}
}