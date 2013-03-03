Vs. Vagrant
===
You might ask yourself "why would I use this rather than vagrant". The answer is "you almost certainly wouldn't", I know I would use Vagrant instead. This isn't intended for production use. It's more of an investigation and learning tool for me to learn about Virtual machines, networking (which is a pain) and provisioning.

Here are a few differences:

                                  | Vagarant                    | VBox Java API                |
    |-----------------------------+-----------------------------+------------------------------|
    | State                       | Mature & Common             | Immature                     |
    | Language                    | Ruby                        | Java                         |
    | Configuration               | Ruby                        | XML                          |
    | Base Boxes                  | VeeWee                      | In Built                     |
    | Provisioning                | Chef or Puppet              | SSH / Patches                |
    | Interface                   | Command Line                | Ant or Maven                 |