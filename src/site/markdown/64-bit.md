32 Bit vs 64 Bit
===
Currently the definitions are all 32 bit. I _think_ you'll want to use the same bits on the guest as the host. It'll be faster.

If you want use 64 bit you typically need to:

- Ensure hardware virtualizaiton is enabled on the host (see (http://www.parallels.com/uk/products/novt))
- Append "_64" to the OS type, e.g. "RedHat_64".
- Enable IO ACPI (as a side-effect, it'll be much faster, if your host OS is 64 bit).
- Use a 64 ISO (note that Windows will install the appropriate kernel for you, but you cannot change it once it's installed).

To save time, a patch is provided that will detect the host arch and apply a patch to the guest, e.g.:

    <patches>
        <archPatch/>
    </patches>

The patch will make the appropriate changes and choose an ISO for you.

