# Twitter to SNS

## Usage:

Clone, then set .m2/settings.xml or edit pom.xml properties to add your keys

Then, run package.sh and use target/twtail as your main binary.

Syntax: twtail [AWS TOPIC ARN]

If Topic Arn is enabled, messages will be published to SNS as well

## TODO

  * Command Line Parsing
  * Service Scripts
  * Last Fetched Id
  * Runtime (Error / Success Metrics)
