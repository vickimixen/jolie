/*
 *   Copyright (C) 2017 by Stefano Pio Zingaro <stefanopio.zingaro@unibo.it>  
 *   Copyright (C) 2017 by Saverio Giallorenzo <saverio.giallorenzo@gmail.com>
 *                                                                             
 *   This program is free software; you can redistribute it and/or modify      
 *   it under the terms of the GNU Library General Public License as           
 *   published by the Free Software Foundation; either version 2 of the        
 *   License, or (at your option) any later version.                           
 *                                                                             
 *   This program is distributed in the hope that it will be useful,           
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of            
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             
 *   GNU General Public License for more details.                              
 *                                                                             
 *   You should have received a copy of the GNU Library General Public         
 *   License along with this program; if not, write to the                     
 *   Free Software Foundation, Inc.,                                           
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.                 
 *                                                                             
 *   For details about the authors of this software, see the AUTHORS file.     
 */
package jolie.net.coap;

public abstract class ContentFormat {

    public static final long UNDEFINED = -1;
    public static final long TEXT_PLAIN_UTF8 = 0;
    public static final long APP_LINK_FORMAT = 40;
    public static final long APP_XML = 41;
    public static final long APP_OCTET_STREAM = 42;
    public static final long APP_EXI = 47;
    public static final long APP_JSON = 50;
    public static final long APP_RDF_XML = 201;
    public static final long APP_TURTLE = 202;
    public static final long APP_N3 = 203;
    public static final long APP_SHDT = 205;
}
