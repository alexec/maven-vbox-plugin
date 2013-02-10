32 Bit vs 64 Bit
===
Currently the definitions are all 32 bit. I _think_ you'll want to use the same bits on the guest as the host. It'll be faster.

If you want use 64 bit you typically need to:

- Ensure hardware virtualizaiton is enabled on the host (see (http://www.parallels.com/uk/products/novt))
- Append "_64" to the OS type, e.g. "RedHat_64".
- Enable IO ACPI (as a side-effect, it'll be much faster, if your host OS is 64 bit).
