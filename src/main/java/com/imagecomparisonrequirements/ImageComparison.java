package com.imagecomparisonrequirements;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;


import static com.imagecomparisonrequirements.FileManager.*;
import static com.imagecomparisonrequirements.ImageComparisonTools.*;
import static java.awt.Color.RED;

public class ImageComparison {
	private static BufferedImage image1=null;
	private static BufferedImage image2=null;
	private static int[][] matrix;
	  public static int threshold = 5;
	  private int COUNTER = 2;
	  private int regionCount = COUNTER;


	ImageComparison( String image1Name, String image2Name ) throws IOException, URISyntaxException {
		image1 = readImageFromResources( image1Name );
		image2 = readImageFromResources( image2Name );
		matrix = populateTheMatrixOfTheDifferences( image1, image2 );
	}

	private void groupRegions() {
		for ( int row = 0; row < matrix.length; row++ ) {
			for ( int col = 0; col < matrix[row].length; col++ ) {
				if ( matrix[row][col] == 1 ) {
					joinToRegion( row, col );
					regionCount++;
}
			}
					}
					}
	private void joinToRegion( int row, int col ) {
		if ( row < 0 || row >= matrix.length || col < 0 || col >= matrix[row].length || matrix[row][col] != 1 ) return;

		matrix[row][col] = regionCount;

		for ( int i = 0; i < threshold; i++ ) {
			// goes to all directions.
			joinToRegion( row - 1 - i, col );
			joinToRegion( row + 1 + i, col );
			joinToRegion( row, col - 1 - i );
			joinToRegion( row, col + 1 + i );

			joinToRegion( row - 1 - i, col - 1 - i );
			joinToRegion( row + 1 + i, col - 1 - i );
			joinToRegion( row - 1 - i, col + 1 + i );
			joinToRegion( row + 1 + i, col + 1 + i );
		}
	}


	    private static void drawRectangles(int[][] matrix, Graphics2D graphics, int counter, int lastNumberCount) {
	        if (counter > lastNumberCount) return;
	        Rectangle rectangle = createRectangle(matrix, counter);
	        graphics.drawRect(rectangle.getMinY(), rectangle.getMinX(), rectangle.getWidth(), rectangle.getHeight());
	        drawRectangles(matrix, graphics, ++counter, lastNumberCount);
	    }

	BufferedImage compareImages() throws IOException, URISyntaxException {


		checkCorrectImageSize( image1, image2 );

		BufferedImage outImg = deepCopy( image2 );

		Graphics2D graphics = outImg.createGraphics();
		graphics.setColor( RED );

		groupRegions();
		drawRectangles( graphics );


		saveImage( "result/result2.png", outImg );

		return outImg;
	}

	private void drawRectangles( Graphics2D graphics ) {
		if( COUNTER > regionCount ) return;

		Rectangle rectangle = createRectangle( matrix, COUNTER );

		graphics.drawRect( rectangle.getMinY(), rectangle.getMinX(), rectangle.getWidth(), rectangle.getHeight() );
		COUNTER++;
		drawRectangles( graphics );
	}
	public static void main(String[] args)throws IOException, URISyntaxException  {
		ImageComparison comparison = new ImageComparison( "image1.png", "image2.png" );
		createGUI( comparison.compareImages() );
	}
}
