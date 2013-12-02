#ifndef TCBHSM_CONFIGURATION_H
#define TCBHSM_CONFIGURATION_H

#include <string>
#include <vector>

namespace communication
{
class Connection;
}

namespace hsm
{
class Configuration
{
public:
    struct RabbitMqConf {
        std::string hostname;
        std::string port;
        std::string rpcQueue;
    };

    struct SlotConf {
        std::string label;
        std::string userPin;
        std::string soPin;
    };

    struct DatabaseConf { // TODO: review this.
        std::string path;
    };
    
    Configuration() = default;
    Configuration ( std::string configurationPath );
    Configuration ( Configuration & ) = default;
    Configuration ( Configuration && ) = default;    
    Configuration & operator=(Configuration &) = default;
    Configuration & operator=(Configuration &&) = default;

    virtual RabbitMqConf const & getRabbitMqConf() const;
    virtual std::vector<SlotConf> const & getSlotConf() const;
    virtual DatabaseConf const & getDatabaseConf() const;
    virtual void load(std::string configurationPath);

    virtual ~Configuration() = default;
private:
    RabbitMqConf rabbitMqConf_;
    std::vector<SlotConf> slotConf_;
    DatabaseConf databaseConf_;
};
}

#endif // TCBHSM_CONFIGURATION_H
