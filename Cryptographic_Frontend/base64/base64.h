#include <string>
namespace base64 {
  std::string encode(unsigned char const* , unsigned int len);
  std::string decode(std::string const& s);
}