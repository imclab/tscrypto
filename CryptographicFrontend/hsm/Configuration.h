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

    RabbitMqConf const & getRabbitMqConf() const;
    std::vector<SlotConf> const & getSlotConf() const;
    DatabaseConf const & getDatabaseConf() const;

    Configuration() = delete;
    Configuration ( std::string configurationPath );
private:
    RabbitMqConf rabbitMqConf_;
    std::vector<SlotConf> slotConf_;
    DatabaseConf databaseConf_;
};
}

#endif // TCBHSM_CONFIGURATION_H
