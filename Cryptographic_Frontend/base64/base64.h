#include <string>
namespace base64 {
  using TCHAR = char;
  using DWORD = long;
  using BYTE = unsigned char;

  std::basic_string<TCHAR> encode(std::vector<BYTE> inputBuffer);
  std::string encode(BYTE const* , unsigned int len);
  
  std::string decode(std::string const& s);
  std::vector<BYTE> decodeToBytes(const std::basic_string<TCHAR>& input);
}