import {
  Controller,
  type Control,
  type FieldPath,
  type FieldValues,
} from "react-hook-form";

import { Field, FieldContent, FieldError } from "@/components/ui/field";
import { Switch } from "@/components/ui/switch";
import { FieldHeader } from "@/components/custom/elements/form/field-header";

interface RHFSwitchProps<T extends FieldValues> {
  name: FieldPath<T>;
  control: Control<T>;

  label?: string;
  description?: string;
  icon?: React.ReactNode;

  disabled?: boolean;
  required?: boolean;
  className?: string;
}

export function RHFSwitch<T extends FieldValues>({
  name,
  control,
  label,
  description,
  icon,
  disabled,
  required,
  className,
}: RHFSwitchProps<T>) {
  return (
    <Controller
      name={name}
      control={control}
      render={({ field, fieldState: { invalid, error } }) => (
        <Field
          orientation="horizontal"
          data-invalid={invalid}
          className={className}
        >
          <FieldContent>
            <FieldHeader
              label={label}
              description={description}
              icon={icon}
              required={required}
            />

            {invalid && <FieldError errors={[error]} />}
          </FieldContent>

          <Switch
            dir="ltr"
            id={field.name}
            name={field.name}
            checked={!!field.value}
            disabled={disabled}
            onCheckedChange={field.onChange}
            aria-invalid={invalid}
          />
        </Field>
      )}
    />
  );
}
