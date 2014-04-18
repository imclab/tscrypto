/*
   Copyright 2013 NIC Chile Research Labs.
   This file is part of PKCS11-TsCrypto.

   PKCS11-TsCrypto is free software: you can redistribute it and/or modify
   it under the terms of the GNU Lesser General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   PKCS11-TsCrypto is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public License
   along with PKCS11-TsCrypto.  If not, see <http://www.gnu.org/licenses/>.

*/

#include <stdexcept>
#include <string>
#include <vector>
#include "base64.h"


namespace base64
{

  namespace
  {
    inline bool is_base64 ( unsigned char c )
    {
      return ( isalnum ( c ) || ( c == '+' ) || ( c == '/' ) );
    }

    const char encodeLookup[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    const char padCharacter = '=';
  }

  string encode ( vector<byte> inputBuffer )
  {
    string encodedString;   
    encodedString.reserve ( ( ( inputBuffer.size() /3 ) + ( inputBuffer.size() % 3 > 0 ) ) * 4 );
    uint64_t temp;
    vector<byte>::iterator cursor = inputBuffer.begin();
    for ( size_t idx = 0; idx < inputBuffer.size() /3; idx++ ) {
      temp  = ( *cursor++ ) << 16; //Convert to big endian
      temp += ( *cursor++ ) << 8;
      temp += ( *cursor++ );
      encodedString.append ( 1,encodeLookup[ ( temp & 0x00FC0000 ) >> 18] );
      encodedString.append ( 1,encodeLookup[ ( temp & 0x0003F000 ) >> 12] );
      encodedString.append ( 1,encodeLookup[ ( temp & 0x00000FC0 ) >> 6 ] );
      encodedString.append ( 1,encodeLookup[ ( temp & 0x0000003F )      ] );
    }
    switch ( inputBuffer.size() % 3 ) {
      case 1:
        temp  = ( *cursor++ ) << 16; //Convert to big endian
        encodedString.append ( 1,encodeLookup[ ( temp & 0x00FC0000 ) >> 18] );
        encodedString.append ( 1,encodeLookup[ ( temp & 0x0003F000 ) >> 12] );
        encodedString.append ( 2,padCharacter );
        break;
      case 2:
        temp  = ( *cursor++ ) << 16; //Convert to big endian
        temp += ( *cursor++ ) << 8;
        encodedString.append ( 1,encodeLookup[ ( temp & 0x00FC0000 ) >> 18] );
        encodedString.append ( 1,encodeLookup[ ( temp & 0x0003F000 ) >> 12] );
        encodedString.append ( 1,encodeLookup[ ( temp & 0x00000FC0 ) >> 6 ] );
        encodedString.append ( 1,padCharacter );
        break;
    }
    return encodedString;
  }

  vector<byte> decode ( const string& input )
  {
    if ( input.length() % 4 ) { //Sanity check
      throw std::runtime_error ( "Non-Valid base64!" );
    }
    size_t padding = 0;
    if ( input.length() ) {
      if ( input[input.length()-1] == padCharacter ) {
        padding++;
      }
      if ( input[input.length()-2] == padCharacter ) {
        padding++;
      }
    }
    //Setup a vector to hold the result
    vector<byte> decodedBytes;
    decodedBytes.reserve ( ( ( input.length() /4 ) *3 ) - padding );
    uint64_t temp=0; //Holds decoded quanta
    string::const_iterator cursor = input.begin();
    while ( cursor < input.end() ) {
      for ( size_t quantumPosition = 0; quantumPosition < 4; quantumPosition++ ) {
        temp <<= 6;
        if ( *cursor >= 0x41 && *cursor <= 0x5A ) {   // This area will need tweaking if
          temp |= *cursor - 0x41;    // you are using an alternate alphabet
        } else if ( *cursor >= 0x61 && *cursor <= 0x7A ) {
          temp |= *cursor - 0x47;
        } else if ( *cursor >= 0x30 && *cursor <= 0x39 ) {
          temp |= *cursor + 0x04;
        } else if ( *cursor == 0x2B ) {
          temp |= 0x3E;    //change to 0x2D for URL alphabet
        } else if ( *cursor == 0x2F ) {
          temp |= 0x3F;    //change to 0x5F for URL alphabet
        } else if ( *cursor == padCharacter ) { //pad
          switch ( input.end() - cursor ) {
            case 1: //One pad character
              decodedBytes.push_back ( ( temp >> 16 ) & 0x000000FF );
              decodedBytes.push_back ( ( temp >> 8 ) & 0x000000FF );
              return decodedBytes;
            case 2: //Two pad characters
              decodedBytes.push_back ( ( temp >> 10 ) & 0x000000FF );
              return decodedBytes;
            default:
              throw std::runtime_error ( "Invalid Padding in Base 64!" );
          }
        }  else {
          throw std::runtime_error ( "Non-Valid Character in Base 64!" );
        }
        cursor++;
      }
      decodedBytes.push_back ( ( temp >> 16 ) & 0x000000FF );
      decodedBytes.push_back ( ( temp >> 8 ) & 0x000000FF );
      decodedBytes.push_back ( ( temp ) & 0x000000FF );
    }
    return decodedBytes;
  }

  string encode ( byte const* bytes_to_encode, size_t in_len )
  {
    return encode ( vector<byte> ( bytes_to_encode, bytes_to_encode + in_len ) );
  }

  byte * decode ( string const& encoded_string, size_t * out_len )
  {
    vector<byte> result = decode ( encoded_string );
    byte * out = new byte[result.size()];
    for(byte b : result) {
      *out = b;
    }
    return out;
  }

}
